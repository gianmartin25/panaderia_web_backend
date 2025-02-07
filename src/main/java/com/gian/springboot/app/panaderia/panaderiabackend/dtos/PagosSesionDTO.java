package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

public class PagosSesionDTO {
    private Long ordenId;
    private ProductoCartRequestDTO[] productos;

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    public ProductoCartRequestDTO[] getProductos() {
        return productos;
    }

    public void setProductos(ProductoCartRequestDTO[] productos) {
        this.productos = productos;
    }


}
