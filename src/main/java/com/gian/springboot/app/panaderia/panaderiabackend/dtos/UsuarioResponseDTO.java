package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String tipoUsuario;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }



    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String username, String email, String tipoUsuario) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

}
