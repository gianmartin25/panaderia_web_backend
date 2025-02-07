package com.gian.springboot.app.panaderia.panaderiabackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @JsonProperty("personaId")
    public Long getPersonaId() {
        return persona != null ? persona.getId() : null;
    }

    @JsonProperty("apellidos")
    public String getPersonaApellidos() {
        return persona != null ? persona.getApellidos() : null;
    }

    @JsonProperty("fechaNacimiento")
    public LocalDate getPersonaFechaNacimiento() {
        return persona != null ? persona.getFechaNacimiento() : null;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Column(name = "nombres")
    private String nombres;

    @ColumnDefault("false")
    @Column(name = "eliminado")
    private Boolean eliminado = false;

    @Column(name = "id_cargo_empleado")
    private Long idCargoEmpleado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Long getIdCargoEmpleado() {
        return idCargoEmpleado;
    }

    public void setIdCargoEmpleado(Long idCargoEmpleado) {
        this.idCargoEmpleado = idCargoEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}