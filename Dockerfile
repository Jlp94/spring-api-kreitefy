# =====================================================================
#  Kreitefy API — Multi-Stage Dockerfile
#  Stage 1 : Build the JAR with Maven
#  Stage 2 : Run with a minimal JRE image
# =====================================================================

# ── Stage 1: Build ───────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom first (layer-cache friendly)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source and build, skipping tests (tests run in CI)
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ── Stage 2: Runtime ─────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Non-root user for security
RUN addgroup -S kreitefy && adduser -S kreitefy -G kreitefy

WORKDIR /app

# Copy only the executable JAR from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Switch to non-root user
USER kreitefy

# Expose the application port
EXPOSE 8080

# ── Environment variables (override via docker run -e or compose) ────
ENV SPRING_PROFILES_ACTIVE=prod \
    JWT_SECRET=changeme-use-a-strong-secret-in-production \
    GOOGLE_CLIENT_ID=your-google-client-id \
    GOOGLE_CLIENT_SECRET=your-google-client-secret \
    GITHUB_CLIENT_ID=your-github-client-id \
    GITHUB_CLIENT_SECRET=your-github-client-secret \
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/kreitefy \
    SPRING_DATASOURCE_USERNAME=kreitefy \
    SPRING_DATASOURCE_PASSWORD=changeme

# Health-check using netcat to verify the port is open and listening
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD nc -z localhost 8080 || exit 1

ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
