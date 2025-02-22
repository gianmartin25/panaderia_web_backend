package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Persona;
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
        Usuario usuario = usuarioRepository.findByEmail(email);
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();

        List<Cliente> clientes = usuario.getCliente();

        if (clientes != null) {
            usuarioResponseDTO.setTipoCliente(clientes.get(0).getTipoCliente().getNombre());
        }

        if (usuario != null && this.passwordEncryptionService.matches(password, usuario.getPassword())) {
            usuarioResponseDTO.setId(usuario.getId());
            usuarioResponseDTO.setUsername(usuario.getUsername());
            usuarioResponseDTO.setEmail(usuario.getEmail());
            usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());
            return usuarioResponseDTO;
        }
        throw new RuntimeException("Invalid credentials.");
    }

//    public UsuarioResponseDTO authenticatePerson(String email, String password) {
//        Usuario usuario = usuarioRepository.findByEmailAndTipoUsuario(email, "client");
//        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
//        if (usuario != null && this.passwordEncryptionService.matches(password, usuario.getPassword())) {
//            usuarioResponseDTO.setId(usuario.getId());
//            usuarioResponseDTO.setUsername(usuario.getUsername());
//            usuarioResponseDTO.setEmail(usuario.getEmail());
//            usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());
//            return usuarioResponseDTO;
//        }
//         throw new RuntimeException("Invalid credentials.");
//    }
//
//    public UsuarioResponseDTO authenticateCompany(String email, String password) {
//        Usuario usuario = usuarioRepository.findByEmailAndTipoUsuario(email, "client");
//        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
//        if (usuario != null && this.passwordEncryptionService.matches(password, usuario.getPassword())) {
//            usuarioResponseDTO.setId(usuario.getId());
//            usuarioResponseDTO.setUsername(usuario.getUsername());
//            usuarioResponseDTO.setEmail(usuario.getEmail());
//            usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());
//            return usuarioResponseDTO;
//        }
//        throw new RuntimeException("Invalid credentials.");
//    }
//


}
