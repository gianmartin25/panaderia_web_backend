package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.UsuarioEmpleado;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioEmpleadoService {

    @Autowired
    private UsuarioEmpleadoRepository usuarioEmpleadoRepository;

    public UsuarioEmpleado registrarUsuarioEmpleado(UsuarioEmpleado usuarioEmpleado) {
        return usuarioEmpleadoRepository.save(usuarioEmpleado);
    }
}