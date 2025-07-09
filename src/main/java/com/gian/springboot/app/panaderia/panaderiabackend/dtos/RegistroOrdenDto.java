package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

import java.util.List;

public class RegistroOrdenDto {


    private Long idDireccionEntrega;
    private Long clienteId;
    private List<ProductoCartRequestDTO> productos;

    // Getters and setters

    public Long getIdDireccionEntrega() {
        return idDireccionEntrega;
    }

    public void setIdDireccionEntrega(Long idDireccionEntrega) {
        this.idDireccionEntrega = idDireccionEntrega;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ProductoCartRequestDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCartRequestDTO> productos) {
        this.productos = productos;
    }
}