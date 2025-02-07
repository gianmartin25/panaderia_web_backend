package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

public class RegistroOrdenDto {
    private Long idDireccionEntrega;
    private Long clienteId;
    private ProductoCartRequestDTO[] productos;


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

    public ProductoCartRequestDTO[] getProductos() {
        return productos;
    }

    public void setProductos(ProductoCartRequestDTO[] productos) {
        this.productos = productos;
    }
}
