package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.models.UsuarioEmpleado;
import com.gian.springboot.app.panaderia.panaderiabackend.services.UsuarioEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario-empleado")
public class UsuarioEmpleadoController {

    @Autowired
    private UsuarioEmpleadoService usuarioEmpleadoService;

    @PostMapping("/registrar")
    public UsuarioEmpleado registrarUsuarioEmpleado(@RequestBody UsuarioEmpleado usuarioEmpleado) {
        return usuarioEmpleadoService.registrarUsuarioEmpleado(usuarioEmpleado);
    }
}