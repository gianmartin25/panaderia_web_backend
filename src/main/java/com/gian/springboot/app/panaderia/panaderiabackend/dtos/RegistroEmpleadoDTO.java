package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

import java.time.LocalDate;
import java.util.Date;

public class RegistroEmpleadoDTO {
    private String nombres;
    private String apellidos;
    private String documento;
    private Long tipoDocumento;
    private LocalDate fechaContratacion;
    private LocalDate fechaNacimiento;
    private Long idCargoEmpleado;


    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Long getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Long tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getIdCargoEmpleado() {
        return idCargoEmpleado;
    }

    public void setIdCargoEmpleado(Long idCargoEmpleado) {
        this.idCargoEmpleado = idCargoEmpleado;
    }



}
