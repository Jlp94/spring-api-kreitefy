package com.kreitefy.api.users.domain.models;


import com.kreitefy.api.users.domain.type.RolType;

public record User(
        String username,
        String nombre,
        String apellidos,
        String password,
        String email,
        RolType rol
) {
    public User actualizarPerfil(String nuevoNombre, String nuevosApellidos, String nuevoEmail, String passwordFinal) {
        return new User(
                this.username,
                nuevoNombre,
                nuevosApellidos,
                passwordFinal,
                nuevoEmail,
                this.rol
        );
    }
}
