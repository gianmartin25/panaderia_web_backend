package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.exceptions.AuthException;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncryptionService passwordEncryptionService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncryptionService passwordEncryptionService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    public UsuarioResponseDTO authenticate(String email, String password) {
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getCliente().stream().anyMatch(c -> c.getEmail().equals(email)))
                .findFirst()
                .orElseThrow(() -> new AuthException("Credenciales inválidas.", 401));

        // Check if the account is locked
        if (usuario.getLockoutTime() != null && usuario.getLockoutTime().isAfter(LocalDateTime.now())) {
            throw new AuthException("Cuenta bloqueada. Inténtalo de nuevo más tarde.", 403);
        }

        // Check password
        if (!passwordEncryptionService.matches(password, usuario.getPassword())) {
            int failedAttempts = usuario.getFailedAttempts() + 1;
            usuario.setFailedAttempts(failedAttempts);

            if (failedAttempts >= 3) { // Lock after 3 failed attempts
                usuario.setLockoutTime(LocalDateTime.now().plusMinutes(15)); // Lock for 15 minutes
                usuario.setFailedAttempts(0); // Reset failed attempts
            }

            usuarioRepository.save(usuario);
            throw new AuthException("Credenciales inválidas.", 401);
        }

        usuario.setFailedAttempts(0);
        usuario.setLockoutTime(null);
        usuarioRepository.save(usuario);

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setUsername(usuario.getUsername());
        usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());

        List<Cliente> clientes = usuario.getCliente();
        if (!clientes.isEmpty()) {
            Cliente cliente = clientes.get(0);
            usuarioResponseDTO.setEmail(cliente.getEmail());
            usuarioResponseDTO.setTipoCliente(cliente.getTipoCliente().getNombre());
        }

        return usuarioResponseDTO;
    }
}