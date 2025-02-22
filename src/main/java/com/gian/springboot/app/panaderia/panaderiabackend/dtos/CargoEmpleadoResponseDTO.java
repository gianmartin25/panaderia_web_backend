package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

public class CargoEmpleadoResponseDTO {
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

    private Long id;
    private String nombre;
}
