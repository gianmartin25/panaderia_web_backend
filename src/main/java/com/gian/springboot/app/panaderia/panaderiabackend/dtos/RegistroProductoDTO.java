package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

import java.math.BigDecimal;

public class RegistroProductoDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Long categoriaId;
    private Long proveedorId;

    public RegistroProductoDTO(String nombre, String descripcion, BigDecimal precio, Long categoriaId, Long proveedorId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
        this.proveedorId = proveedorId;
    }

    public RegistroProductoDTO() {
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }



}
