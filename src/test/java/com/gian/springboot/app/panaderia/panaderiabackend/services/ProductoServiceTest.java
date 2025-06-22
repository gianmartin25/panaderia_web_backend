package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Categoria;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Proveedor;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.CategoriaRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarProducto_ShouldSaveProduct_WhenValidDataProvided() throws Exception {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Postres");

        Proveedor proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor A");

        RegistroProductoDTO registroProductoDTO = new RegistroProductoDTO();
        registroProductoDTO.setNombre("Torta de Chocolate");
        registroProductoDTO.setDescripcion("Delicious chocolate cake");
        registroProductoDTO.setPrecio(new BigDecimal(50));
        registroProductoDTO.setCategoriaId(1L);
        registroProductoDTO.setProveedorId(1L);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Torta de Chocolate");
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);

        MultipartFile imagen = mock(MultipartFile.class);
        when(imagen.isEmpty()).thenReturn(false);
        when(imagen.getOriginalFilename()).thenReturn("image.jpg");
        when(imagen.getInputStream()).thenReturn(new ByteArrayInputStream("test image content".getBytes()));

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(productoRepository.existsByNombreIgnoreCase("Torta de Chocolate")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        ProductoDTO result = productoService.guardarProducto(registroProductoDTO, imagen);

        // Assert
        assertNotNull(result);
        assertEquals("Torta de Chocolate", result.getNombre());
        assertEquals(1L, result.getCategoriaId());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void verificarStock_ShouldReturnTrue_WhenStockIsSufficient() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setTotalCantidad(10);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act
        boolean result = productoService.verificarStock(1L, 5);

        // Assert
        assertTrue(result);
    }

    @Test
    void verificarStock_ShouldReturnFalse_WhenStockIsInsufficient() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setTotalCantidad(3);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act
        boolean result = productoService.verificarStock(1L, 5);

        // Assert
        assertFalse(result);
    }

    @Test
    void eliminarProducto_ShouldDeleteProduct_WhenProductExists() {
        // Arrange
        Long productId = 1L;

        // Act
        productoService.eliminarProducto(productId);

        // Assert
        verify(productoRepository, times(1)).deleteById(productId);
    }
}