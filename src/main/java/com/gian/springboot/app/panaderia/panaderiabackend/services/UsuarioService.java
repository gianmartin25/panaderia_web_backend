package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO getUsuarioByEmailAndUsuarioType(String email, String tipoUsuario) {
        Usuario usuario = usuarioRepository.findByEmailAndTipoUsuario(email, tipoUsuario);
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        if (usuario != null) {
            List<Cliente> clientes = usuario.getCliente();
            if (clientes != null) {
                usuarioResponseDTO.setTipoCliente(clientes.get(0).getTipoCliente().getNombre());
            }
            usuarioResponseDTO.setId(usuario.getId());
            usuarioResponseDTO.setUsername(usuario.getUsername());
            usuarioResponseDTO.setEmail(usuario.getEmail());
            usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());
            return usuarioResponseDTO;
        }
        throw new RuntimeException("Invalid credentials.");
    }
}
