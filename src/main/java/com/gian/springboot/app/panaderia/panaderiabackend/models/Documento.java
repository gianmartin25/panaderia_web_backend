package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "documentos")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;

    @Column(name = "numero", nullable = false, length = Integer.MAX_VALUE)
    private String numero;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "persona_id")
//    private Persona persona;
//
//
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "empresa_id")
//    private Empresa empresa;

    @ColumnDefault("false")
    @Column(name = "eliminado")
    private Boolean eliminado = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @OneToOne(mappedBy = "documento")
    private Empresa documentoEmpresa;

    public Empresa getDocumentoEmpresa() {
        return documentoEmpresa;
    }

    public void setDocumentoEmpresa(Empresa documentoEmpresa) {
        this.documentoEmpresa = documentoEmpresa;
    }

    public Persona getDocumentoPersona() {
        return documentoPersona;
    }

    public void setDocumentoPersona(Persona documentoPersona) {
        this.documentoPersona = documentoPersona;
    }

    @OneToOne(mappedBy = "documento")
    private Persona documentoPersona;

//    public Persona getPersona() {
//        return persona;
//    }
//
//    public void setPersona(Persona persona) {
//        this.persona = persona;
//    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }


//    public Empresa getEmpresa() {
//        return empresa;
//    }
//
//    public void setEmpresa(Empresa empresa) {
//        this.empresa = empresa;
//    }


}