package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.InventarioProducto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.InventarioProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioProductoServiceTest {

    @Mock
    private InventarioProductoRepository inventarioProductoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioProductoService inventarioProductoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarInventario_Success() {
        RegistroInventarioProductoDTO registroInventarioProductoDTO = new RegistroInventarioProductoDTO();
        registroInventarioProductoDTO.setCantidad(10);
        registroInventarioProductoDTO.setProductoId(1L);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pan");

        InventarioProducto inventarioProducto = new InventarioProducto();
        inventarioProducto.setId(1L);
        inventarioProducto.setCantidad(10);
        inventarioProducto.setProducto(producto);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(inventarioProductoRepository.save(any(InventarioProducto.class))).thenReturn(inventarioProducto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        InventarioProductoDTO result = inventarioProductoService.registrarMovimientoInventario(registroInventarioProductoDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10, result.getCantidad());
        assertEquals("Pan", result.getProductoName());
        verify(inventarioProductoRepository, times(1)).save(any(InventarioProducto.class));
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testListarInventario_Success() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pan");

        InventarioProducto inventarioProducto = new InventarioProducto();
        inventarioProducto.setId(1L);
        inventarioProducto.setCantidad(10);
        inventarioProducto.setProducto(producto);

        when(inventarioProductoRepository.findAll()).thenReturn(Collections.singletonList(inventarioProducto));

        List<InventarioProductoDTO> result = inventarioProductoService.listarInventario();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(10, result.get(0).getCantidad());
        assertEquals("Pan", result.get(0).getProductoName());
        verify(inventarioProductoRepository, times(1)).findAll();
    }
}