package com.kreitefy.api.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="ESTILO_MUSICAL")
public class StyleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estilo_seq")
    @SequenceGenerator(name = "estilo_seq", sequenceName = "estilo_seq", allocationSize = 50)
    private Long id;

    @Column(name = "estilo", nullable = false, unique = true)
    private String estilo;

    public StyleEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }


}
