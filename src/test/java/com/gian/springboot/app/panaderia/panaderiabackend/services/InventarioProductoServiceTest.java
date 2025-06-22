package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.InventarioProducto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoMovimiento;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.InventarioProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.TipoMovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioProductoServiceTest {

    @Mock
    private InventarioProductoRepository inventarioProductoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private TipoMovimientoRepository tipoMovimientoRepository;

    @InjectMocks
    private InventarioProductoService inventarioProductoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarMovimientoInventario_SuccessIngreso() {
        // Arrange
        RegistroInventarioProductoDTO registroDTO = new RegistroInventarioProductoDTO();
        registroDTO.setProductoId(1L);
        registroDTO.setTipoMovimientoId(1L);
        registroDTO.setCantidad(10);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pan");
        producto.setTotalCantidad(50);

        TipoMovimiento tipoMovimiento = new TipoMovimiento();
        tipoMovimiento.setId(1L);
        tipoMovimiento.setNombre("INGRESO");

        InventarioProducto inventarioProducto = new InventarioProducto();
        inventarioProducto.setId(1L);
        inventarioProducto.setProducto(producto);
        inventarioProducto.setCantidad(10);
        inventarioProducto.setTipoMovimiento(tipoMovimiento);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(tipoMovimientoRepository.findById(1L)).thenReturn(Optional.of(tipoMovimiento));
        when(inventarioProductoRepository.save(any(InventarioProducto.class))).thenReturn(inventarioProducto);

        // Act
        InventarioProductoDTO result = inventarioProductoService.registrarMovimientoInventario(registroDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10, result.getCantidad());
        assertEquals("Pan", result.getProductoName());
        assertEquals("INGRESO", result.getTipoMovimientoNombre());
        verify(productoRepository, times(1)).findById(1L);
        verify(tipoMovimientoRepository, times(1)).findById(1L);
        verify(inventarioProductoRepository, times(1)).save(any(InventarioProducto.class));
    }

    @Test
    void testRegistrarMovimientoInventario_FailureStockInsuficiente() {
        RegistroInventarioProductoDTO registroDTO = new RegistroInventarioProductoDTO();
        registroDTO.setProductoId(1L);
        registroDTO.setTipoMovimientoId(2L);
        registroDTO.setCantidad(60);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pan");
        producto.setTotalCantidad(50);

        TipoMovimiento tipoMovimiento = new TipoMovimiento();
        tipoMovimiento.setId(2L);
        tipoMovimiento.setNombre("SALIDA");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(tipoMovimientoRepository.findById(2L)).thenReturn(Optional.of(tipoMovimiento));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioProductoService.registrarMovimientoInventario(registroDTO);
        });

        assertEquals("Stock insuficiente para realizar la salida", exception.getMessage());
        verify(productoRepository, times(1)).findById(1L);
        verify(tipoMovimientoRepository, times(1)).findById(2L);
        verify(inventarioProductoRepository, never()).save(any(InventarioProducto.class));
    }
}