package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("now()")
    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_entrega_id")
    private DireccionEntrega direccionEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoOrden estado;

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }


    @Column
    private Boolean pagado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    public Orden() {
        this.detalles = new ArrayList<>();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Transportista transportista;

    public DireccionEntrega getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(DireccionEntrega direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }


    public List<DetalleOrden> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrden> detalles) {
        this.detalles = detalles;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "orden_id")
    private List<DetalleOrden> detalles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }


    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    @PrePersist
    @PreUpdate
    private void preGuardar() {
        this.calcularTotal();
    }

    public void calcularTotal() {
        if (detalles != null && !detalles.isEmpty()) {
            this.total = detalles.stream()
                    .map(DetalleOrden::getSubtotal)
                    .filter(subTotal -> subTotal != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.total = BigDecimal.ZERO;
        }
    }


}