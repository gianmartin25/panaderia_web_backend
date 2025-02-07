package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comprobante_id", referencedColumnName = "id")
    private ComprobantePago comprobantesPago;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    private int numero;
    private Double IGV = 0.18;
    private BigDecimal montoIgv;
    private BigDecimal subTotal;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public BigDecimal getMontoIgv() {
        return this.montoIgv;
    }

    public BigDecimal calcularMontoIgv(BigDecimal total) {
        this.montoIgv = total.multiply(BigDecimal.valueOf(this.IGV));
        return this.montoIgv;
    }

    public void setMontoIgv(BigDecimal montoIgv) {
        this.montoIgv = montoIgv;
    }

    public Double getIGV() {
        return IGV;
    }

    public void setIGV(Double IGV) {
        this.IGV = IGV;
    }

    public BigDecimal getSubTotal() {
        return this.subTotal;
    }

    public BigDecimal calcularSubTotal(BigDecimal total) {
        this.subTotal = total.subtract(this.montoIgv);
        return this.subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComprobantePago getComprobantesPago() {
        return comprobantesPago;
    }

    public void setComprobantesPago(ComprobantePago comprobantesPago) {
        this.comprobantesPago = comprobantesPago;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}