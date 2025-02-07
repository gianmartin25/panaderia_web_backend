package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobanteProductoDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BoletaDTO {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontoIgv() {
        return montoIgv;
    }

    public void setMontoIgv(BigDecimal montoIgv) {
        this.montoIgv = montoIgv;
    }


    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoSubtotal() {
        return montoSubtotal;
    }

    public void setMontoSubtotal(BigDecimal montoSubtotal) {
        this.montoSubtotal = montoSubtotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDistritoFiscal() {
        return distritoFiscal;
    }

    public void setDistritoFiscal(String distritoFiscal) {
        this.distritoFiscal = distritoFiscal;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getRazonSocialFiscal() {
        return razonSocialFiscal;
    }

    public void setRazonSocialFiscal(String razonSocialFiscal) {
        this.razonSocialFiscal = razonSocialFiscal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    private Long id;
    private String numero;
    private String fecha;
    private String nombreCompleto;
    private String direccion;
    private String Dni;
    private String razonSocialFiscal;
    private String direccionFiscal;
    private String distritoFiscal;
    private String fechaEmision;
    private String fechaVencimiento;
    private String moneda;
    private BigDecimal montoTotal;
    private BigDecimal montoSubtotal;
    private BigDecimal montoIgv;

    public List<ComprobanteProductoDTO> getProductos() {
        return productos;
    }


    public void agregarProducto(ComprobanteProductoDTO producto) {
        productos.add(producto);
    }

    private List<ComprobanteProductoDTO> productos = new ArrayList<>();
}

//public class BoletaDTO {
//    private Long id;
//    private String numero;
//    private String fecha;
//    private String nombreCompleto;
//    private String direccion;
//    private String correo;
//    private String rucFiscal;
//    private String razonSocialFiscal;
//    private String direccionFiscal;
//    private String distritoFiscal;
//    private String fechaEmision;
//    private String fechaVencimiento;
//    private String moneda;
//    private String montoTotal;
//    private String montoSubtotal;
//    private String descuento;
//    private String montoIgv;
//}