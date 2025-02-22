package com.gian.springboot.app.panaderia.panaderiabackend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "cargos_empleado")
public class CargoEmpleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", length = Integer.MAX_VALUE)
    private String nombre;


    public List<Empleado> getEmployees() {
        return empleados;
    }

    public void setEmployees(List<Empleado> employees) {
        this.empleados = employees;
    }

    @OneToMany(mappedBy = "cargoEmpleado", cascade = CascadeType.ALL, orphanRemoval = true)

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Empleado> empleados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}