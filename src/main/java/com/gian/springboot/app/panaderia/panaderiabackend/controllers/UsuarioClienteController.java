package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioEmpresaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioPersonaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.services.UsuarioEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioClienteController {

    @Autowired
    private UsuarioEmpresaService usuarioEmpresaService;

    @PostMapping("/empresa")
    public ResponseEntity<Usuario> crearUsuarioEmpresa(@RequestBody RegistroUsuarioEmpresaDTO registroUsuarioEmpresaDTO) {
        Usuario nuevoUsuario = usuarioEmpresaService.crearUsuarioEmpresa(registroUsuarioEmpresaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PostMapping("/persona")
    public ResponseEntity<Usuario> crearUsuarioPersona(@RequestBody RegistroUsuarioPersonaDTO registroUsuarioPersonaDTO) {
        Usuario nuevoUsuario = usuarioEmpresaService.crearUsuarioPersona(registroUsuarioPersonaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @DeleteMapping()
    public ResponseEntity<Map<String, String>> eliminarUsuario() {
        usuarioEmpresaService.eliminarUsuarios();
        Map<String, String> response = Map.of("message", "Usuarios eliminados");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}