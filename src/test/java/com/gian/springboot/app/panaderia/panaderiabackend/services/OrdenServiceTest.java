package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.*;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private DireccionEntregaRepository direccionEntregaRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private TransportistaRepository transportistaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearOrden_Success() {
        // Arrange
        RegistroOrdenDto registroOrdenDto = new RegistroOrdenDto();
        registroOrdenDto.setClienteId(1L);
        registroOrdenDto.setIdDireccionEntrega(1L);

        ProductoCartRequestDTO productoCart = new ProductoCartRequestDTO();
        productoCart.setIdProducto(1L);
        productoCart.setCantidad(2);
        ProductoCartRequestDTO[] productos = new ProductoCartRequestDTO[1];
        productos[0] = productoCart;
        registroOrdenDto.setProductos(productos);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pan");
        producto.setPrecio(new BigDecimal("5.00"));

        DireccionEntrega direccionEntrega = new DireccionEntrega();
        direccionEntrega.setId(1L);

        Empleado empleado = new Empleado();
        empleado.setId(1L);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Transportista transportista = new Transportista();
        transportista.setId(1L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(direccionEntregaRepository.findById(1L)).thenReturn(Optional.of(direccionEntrega));
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(transportistaRepository.findById(1L)).thenReturn(Optional.of(transportista));
        when(ordenRepository.save(any(Orden.class))).thenAnswer(invocation -> {
            Orden savedOrden = invocation.getArgument(0);
            savedOrden.setId(1L); // Simulate database-generated ID
            return savedOrden;
        });

        // Act
        OrdenResponseDto result = ordenService.crearOrden(registroOrdenDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("10.00", result.getTotal());
        verify(productoRepository, times(1)).findById(1L);
        verify(direccionEntregaRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).findById(1L);
        verify(transportistaRepository, times(1)).findById(1L);
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }
}