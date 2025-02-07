package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "transportistas")
public class Transportista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

//    @MapsId
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @ColumnDefault("nextval('transportistas_id_seq')")
//    @JoinColumn(name = "id", nullable = false)
//    private Orden ordenes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @Column(name = "licencia", length = Integer.MAX_VALUE)
    private String licencia;

    @Column(name = "vehiculo", length = Integer.MAX_VALUE)
    private String vehiculo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Orden getOrdenes() {
//        return ordenes;
//    }
//
//    public void setOrdenes(Orden ordenes) {
//        this.ordenes = ordenes;
//    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

}