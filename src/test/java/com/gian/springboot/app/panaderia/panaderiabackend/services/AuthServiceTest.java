package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncryptionService passwordEncryptionService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_Success() {
        String email = "test@example.com";
        String password = "password";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail(email);
        usuario.setPassword("encryptedPassword");
        usuario.setTipoUsuario("client");

        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
        when(passwordEncryptionService.matches(password, "encryptedPassword")).thenReturn(true);

        UsuarioResponseDTO result = authService.authenticate(email, password);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals("client", result.getTipoUsuario());
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        String email = "test@example.com";
        String password = "password";

        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticate(email, password);
        });

        assertEquals("Invalid credentials.", exception.getMessage());
    }
}