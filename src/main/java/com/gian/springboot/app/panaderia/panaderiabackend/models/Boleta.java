package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "boletas")
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comprobante_id",referencedColumnName = "id")
    private ComprobantePago comprobantesPago;


    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    private int numero;

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

//    @Transient
//    public String getNumeroCompleto() {
//        return serie + "-" + String.format("%06d", numeroBoleta); // Ejemplo: B001-000123
//    }
}