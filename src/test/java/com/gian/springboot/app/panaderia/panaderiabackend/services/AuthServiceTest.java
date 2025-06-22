package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.exceptions.AuthException;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoCliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

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
    void authenticate_ShouldReturnUsuarioResponseDTO_WhenCredentialsAreValid() {
        String email = "test@example.com";
        String password = "password";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("encryptedPassword");
        usuario.setFailedAttempts(0);
        usuario.setLockoutTime(null);

        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        TipoCliente tipoCliente = new TipoCliente();
        tipoCliente.setNombre("Regular");
        cliente.setTipoCliente(tipoCliente);
        usuario.setCliente(Collections.singletonList(cliente));

        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));
        when(passwordEncryptionService.matches(password, "encryptedPassword")).thenReturn(true);

        // Act
        UsuarioResponseDTO response = authService.authenticate(email, password);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals("Regular", response.getTipoCliente());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void authenticate_ShouldThrowAuthException_WhenUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "password";

        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> authService.authenticate(email, password));
        assertEquals("Credenciales inválidas.", exception.getMessage());
    }

    @Test
    void authenticate_ShouldThrowAuthException_WhenAccountIsLocked() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Usuario usuario = new Usuario();
        usuario.setLockoutTime(LocalDateTime.now().plusMinutes(10));

        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        usuario.setCliente(Collections.singletonList(cliente));

        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        AuthException exception = assertThrows(AuthException.class, () -> authService.authenticate(email, password));
        assertEquals("Cuenta bloqueada. Inténtalo de nuevo más tarde.", exception.getMessage());
    }

    @Test
    void authenticate_ShouldThrowAuthException_WhenPasswordIsInvalid() {
        String email = "test@example.com";
        String password = "wrongPassword";
        Usuario usuario = new Usuario();
        usuario.setFailedAttempts(0);
        usuario.setLockoutTime(null);
        usuario.setPassword("encryptedPassword");

        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        usuario.setCliente(Collections.singletonList(cliente));

        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));
        when(passwordEncryptionService.matches(password, "encryptedPassword")).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> authService.authenticate(email, password));
        assertEquals("Credenciales inválidas.", exception.getMessage());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}