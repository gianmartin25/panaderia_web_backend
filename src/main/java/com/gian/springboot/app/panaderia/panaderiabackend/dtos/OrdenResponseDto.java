package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

import java.util.List;

public class OrdenResponseDto {
    private Long id;


    private String estadoId;
    private String estado;
    private String fechaCreacion;
    private String fechaEntrega;
    private String direccionEntrega;
    private String cliente;
    private List<ProductoOrdenResponseDTO> productos;
    
    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public String getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(String estadoId) {
        this.estadoId = estadoId;
    }


    private boolean pagado;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private String total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public List<ProductoOrdenResponseDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenResponseDTO> productos) {
        this.productos = productos;
    }
}
