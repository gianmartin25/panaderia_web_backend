package com.gian.springboot.app.panaderia.panaderiabackend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "empresas")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "razon_social", length = Integer.MAX_VALUE)
    private String razonSocial;

    @ColumnDefault("false")
    @Column(name = "eliminado")
    private Boolean eliminado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "documento_id",referencedColumnName = "id")
    private Documento documento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }


    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }


}