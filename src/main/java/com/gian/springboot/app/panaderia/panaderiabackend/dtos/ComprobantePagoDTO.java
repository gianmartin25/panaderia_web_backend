package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

import java.math.BigDecimal;

public class ComprobantePagoDTO {
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public Long getIdPagoTarjeta() {
        return idPagoTarjeta;
    }

    public void setIdPagoTarjeta(Long idPagoTarjeta) {
        this.idPagoTarjeta = idPagoTarjeta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    private BigDecimal monto;
    private Long idOrden;
    private Long idPagoTarjeta;
    private String numero;

}
