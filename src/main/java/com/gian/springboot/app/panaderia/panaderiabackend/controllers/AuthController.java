package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.AuthRequestDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.services.AuthService;
import com.gian.springboot.app.panaderia.panaderiabackend.services.UsuarioService;
import com.gian.springboot.app.panaderia.panaderiabackend.util.JwtUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            UsuarioResponseDTO usuario = authService.authenticate(authRequestDTO.getEmail(), authRequestDTO.getPassword());
            Map<String, Object> claims = new HashMap<>();
            String token = jwtUtil.generateToken(authRequestDTO.getEmail(), usuario.getTipoUsuario());

            claims.put("usuario", usuario);
            claims.put("token", "Bearer " + token);

            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials.");
        }
    }

    @GetMapping("/validate")
    public UsuarioResponseDTO validateToken(@RequestHeader("Authorization") String token) throws BadRequestException {
        boolean isValid = jwtUtil.validateToken(token);
        if (isValid) {
            String email = jwtUtil.extractEmail(token);
            String userType = jwtUtil.extractUserType(token);

            UsuarioResponseDTO usuario = usuarioService.getUsuarioByEmailAndUsuarioType(email, userType);
            return usuario;
        } else {
            throw new BadRequestException("Invalid token.");
        }
    }
}
