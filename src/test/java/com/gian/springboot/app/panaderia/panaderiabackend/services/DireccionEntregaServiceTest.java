package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.DireccionEntregaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroDireccionDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.DireccionEntrega;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ClienteRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.DireccionEntregaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DireccionEntregaServiceTest {

    @Mock
    private DireccionEntregaRepository direccionEntregaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private DireccionEntregaService direccionEntregaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearDireccionEntrega_Success() {
        // Arrange
        RegistroDireccionDTO registroDireccionDTO = new RegistroDireccionDTO();
        registroDireccionDTO.setClienteId(1L);
        registroDireccionDTO.setNombre("John Doe");
        registroDireccionDTO.setDireccion("123 Main St");
        registroDireccionDTO.setCiudad("Springfield");
        registroDireccionDTO.setNumeroDni("12345678");
        registroDireccionDTO.setCodigoPostal("12345");

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        DireccionEntrega direccionEntrega = new DireccionEntrega();
        direccionEntrega.setId(1L);
        direccionEntrega.setNombreReceptor("John Doe");
        direccionEntrega.setDireccion("123 Main St");
        direccionEntrega.setCiudad("Springfield");
        direccionEntrega.setNumeroDni("12345678");
        direccionEntrega.setCodigoPostal(12345L);
        direccionEntrega.setCliente(cliente);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(direccionEntregaRepository.save(any(DireccionEntrega.class))).thenReturn(direccionEntrega);

        // Act
        DireccionEntregaDTO result = direccionEntregaService.crearDireccionEntrega(registroDireccionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getNombre());
        assertEquals("123 Main St", result.getDireccion());
        assertEquals("Springfield", result.getCiudad());
        assertEquals("12345", result.getCodigoPostal());
        assertEquals(1L, result.getClienteId());
        verify(clienteRepository, times(1)).findById(1L);
        verify(direccionEntregaRepository, times(1)).save(any(DireccionEntrega.class));
    }

    @Test
    void testCrearDireccionEntrega_FailureClienteNotFound() {
        // Arrange
        RegistroDireccionDTO registroDireccionDTO = new RegistroDireccionDTO();
        registroDireccionDTO.setClienteId(1L);
        registroDireccionDTO.setCodigoPostal(null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            direccionEntregaService.crearDireccionEntrega(registroDireccionDTO);
        });

        assertEquals("Cliente not found", exception.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
        verify(direccionEntregaRepository, never()).save(any(DireccionEntrega.class));
    }
}