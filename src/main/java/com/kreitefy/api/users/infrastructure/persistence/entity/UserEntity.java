package com.kreitefy.api.users.infrastructure.persistence.entity;

import com.kreitefy.api.users.domain.type.RolType;
import jakarta.persistence.*;


@Entity
@Table(name = "USUARIO")
public class UserEntity {
    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "nombre",nullable = false)
    private String nombre;

    @Column(name = "apellidos",nullable = false)
    private String apellidos;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "rol", nullable = false)
    @Enumerated(EnumType.STRING)
    private RolType rol;

    @Version
    private Integer version;

    public UserEntity() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolType getRol() {
        return rol;
    }

    public void setRol(RolType rol) {
        this.rol = rol;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}