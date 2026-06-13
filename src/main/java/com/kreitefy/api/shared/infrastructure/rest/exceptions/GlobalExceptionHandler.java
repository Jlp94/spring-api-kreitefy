package com.kreitefy.api.shared.infrastructure.rest.exceptions;

import com.kreitefy.api.shared.domain.errors.BadRequestException;
import com.kreitefy.api.shared.domain.errors.ConflictException;
import com.kreitefy.api.shared.domain.errors.NotFoundException;
import com.kreitefy.api.shared.domain.errors.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ─── Helpers para formato unificado ────────────────────────────────

    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", String.valueOf(status.value()),
                "error", status.getReasonPhrase(),
                "message", message
        ));
    }

    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", String.valueOf(status.value()),
                "error", error,
                "message", message
        ));
    }

    // ─── 401 Unauthorized ──────────────────────────────────────────────

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // ─── 400 Bad Request ───────────────────────────────────────────────

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMalformedJson(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido o tiene un formato incorrecto.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";
        String message = String.format("El parámetro '%s' debe ser de tipo %s.", paramName, requiredType);
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String firstError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("Error de validación en los datos enviados.");
        return buildResponse(HttpStatus.BAD_REQUEST, firstError);
    }

    // ─── 404 Not Found ─────────────────────────────────────────────────

    @ExceptionHandler({
            jakarta.persistence.EntityNotFoundException.class,
            org.springframework.orm.jpa.JpaObjectRetrievalFailureException.class,
            NotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFound(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof org.springframework.orm.jpa.JpaObjectRetrievalFailureException) {
            message = "El registro relacionado no existe o ha sido eliminado.";
        }
        return buildResponse(HttpStatus.NOT_FOUND, message);
    }

    // ─── 409 Conflict ──────────────────────────────────────────────────

    @ExceptionHandler({
            ConflictException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<Map<String, String>> handleDomainConflicts(Exception ex) {
        String message;

        if (ex instanceof ConflictException) {
            message = ex.getMessage();
        } else {
            String fkMessage = ex.getMessage() != null ? ex.getMessage() : "";

            if (fkMessage.contains("violates foreign key constraint") || fkMessage.contains("ConstraintViolationException")) {
                message = "No se puede eliminar este registro ya que tiene datos asociados.";
            } else {
                message = "Error: El registro ya existe o los datos son duplicados.";
            }
        }

        return buildResponse(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handleConcurrencyException(ObjectOptimisticLockingFailureException ex) {
        return buildResponse(HttpStatus.CONFLICT, "CONCURRENCY_ERROR",
                "El registro ha sido modificado por otro usuario. Por favor, recarga los datos e inténtalo de nuevo.");
    }

    @ExceptionHandler({
            org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authorization.AuthorizationDeniedException.class
    })
    public ResponseEntity<Map<String, String>> handleAccessDenied(Exception ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Acceso denegado. No tienes permisos para realizar esta acción.");
    }

    // ─── 500 Catch-all (última línea de defensa) ───────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedException(Exception ex) {
        log.error("Error inesperado no controlado: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error interno en el servidor. Por favor, inténtalo más tarde.");
    }
}