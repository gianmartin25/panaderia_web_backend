package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") RegistroProductoDTO productoDTO,
            @RequestPart("imagen") MultipartFile imagen) {
        try {
            ProductoDTO nuevoProducto = productoService.guardarProducto(productoDTO, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException ex) {
            // Captura la excepción y devuelve un mensaje de error con código 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (IOException ex) {
            // Manejo de errores relacionados con la imagen
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al procesar la imagen: " + ex.getMessage()));
        }
    }

    @GetMapping("/pagination")
    public Map<String, Object> listarProductosWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") String categoriaId,
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return productoService.buscarProductosPorNombreYCategoria(page, size, categoriaId, nombre, sortBy, sortDirection);
    }



    @GetMapping
    public List<ProductoDTO> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ProductoDTO>> filtrarProductosPorNombre(@RequestParam String nombre) {
        List<ProductoDTO> productos = productoService.filtrarProductosPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar-stock")
    public ResponseEntity<?> verificarStock(
            @RequestParam Long productoId,
            @RequestParam int cantidad) {
        try {
            boolean stockDisponible = productoService.verificarStock(productoId, cantidad);
            return ResponseEntity.ok(Map.of("productoId", productoId, "stockDisponible", stockDisponible));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }
}

