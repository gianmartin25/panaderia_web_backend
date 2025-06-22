package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.EmpleadoDto;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroEmpleadoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private CargoEmpleadoService cargoEmpleadoService;

    @Mock
    private DocumentoRepository documentoRepository;

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarEmpleado_Success() {
        // Arrange
        RegistroEmpleadoDTO registroEmpleadoDto = new RegistroEmpleadoDTO();
        registroEmpleadoDto.setEmail("test@example.com");
        registroEmpleadoDto.setNombres("John");
        registroEmpleadoDto.setApellidos("Doe");
        registroEmpleadoDto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        registroEmpleadoDto.setDocumento("12345678");
        registroEmpleadoDto.setTipoDocumento(1L);
        registroEmpleadoDto.setIdCargoEmpleado(1L);
        registroEmpleadoDto.setFechaContratacion(LocalDate.of(2023, 1, 1));

        CargoEmpleado cargoEmpleado = new CargoEmpleado();
        cargoEmpleado.setId(1L);
        cargoEmpleado.setNombre("Manager");

        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setId(1L);
        tipoDocumento.setNombre("DNI");

        Persona persona = new Persona();
        persona.setId(1L);
        persona.setApellidos("Doe");

        Documento documento = new Documento();
        documento.setId(1L);
        documento.setTipoDocumento(tipoDocumento);
        documento.setNumero("12345678"); // Ensure documento is set

        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombres("John");
        empleado.setPersona(persona);
        empleado.setCargoEmpleado(cargoEmpleado);

        when(empleadoRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(cargoEmpleadoService.obtenerCargoEmpleado(1L)).thenReturn(cargoEmpleado);
        when(tipoDocumentoRepository.existsById(1L)).thenReturn(true);
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(tipoDocumento));
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);
        when(documentoRepository.findDocumentoById(1L)).thenReturn(documento);

        // Act
        EmpleadoDto result = empleadoService.registrarEmpleado(registroEmpleadoDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getNombres());
        assertEquals("Doe", result.getApellidos());
        assertEquals("DNI", result.getTipoDocumento());
        assertEquals("12345678", result.getDocumento()); // Validate documento
        verify(empleadoRepository, times(1)).existsByEmail("test@example.com");
        verify(cargoEmpleadoService, times(1)).obtenerCargoEmpleado(1L);
        verify(personaRepository, times(1)).save(any(Persona.class));
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }
}