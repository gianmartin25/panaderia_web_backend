package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_orden")
public class EstadoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    public EstadoOrden() {
    }

    public EstadoOrden(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}