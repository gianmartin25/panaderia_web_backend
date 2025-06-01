package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncryptionService passwordEncryptionService;

    public UsuarioResponseDTO authenticate(String email, String password) {
        // Find the user by email through the Cliente entity
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getCliente().stream().anyMatch(c -> c.getEmail().equals(email)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));

        // Check if the account is verified
        if (!usuario.getVerificado()) {
            throw new RuntimeException("Cuenta no verificada. Por favor, verifica tu cuenta antes de iniciar sesión.");
        }

        // Check password
        if (!this.passwordEncryptionService.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas.");
        }

        // Prepare the response DTO
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setUsername(usuario.getUsername());
        usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());

        // Retrieve the email and tipoCliente from the first associated Cliente
        List<Cliente> clientes = usuario.getCliente();
        if (!clientes.isEmpty()) {
            Cliente cliente = clientes.get(0);
            usuarioResponseDTO.setEmail(cliente.getEmail());
            usuarioResponseDTO.setTipoCliente(cliente.getTipoCliente().getNombre());
        }

        return usuarioResponseDTO;
    }
}