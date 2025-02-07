package com.gian.springboot.app.panaderia.panaderiabackend.dtos;

public class AuthResponseDTO {
    private String jwt;

    public AuthResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    // Getter
}