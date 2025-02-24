// PagoTarjeta.java
package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pagos_tarjeta")
public class PagoTarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    @Column(name = "numero_tarjeta", nullable = false, length = Integer.MAX_VALUE)
    private String numeroTarjeta;

    @Column()
    private String marca;

    @Column(name = "titular", nullable = false, length = Integer.MAX_VALUE)
    private String titular;

    @Column(name = "fecha_expiracion", nullable = false)
    private String fechaExpiracion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id", referencedColumnName = "id")
    private Pago pago;


}