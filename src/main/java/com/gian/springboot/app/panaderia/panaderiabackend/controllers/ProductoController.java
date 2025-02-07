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
    public ResponseEntity<ProductoDTO> crearProducto(
            @RequestPart("producto") RegistroProductoDTO productoDTO,
            @RequestPart("imagen") MultipartFile imagen) throws IOException {
        ProductoDTO nuevoProducto = productoService.guardarProducto(productoDTO, imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @GetMapping("/pagination")
    public Map<String, Object> listarProductosWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") String categoriaId,
            @RequestParam(defaultValue = "") String nombre
    ) {
        Map<String, Object> productos = productoService.buscarProductosPorNombreYCategoria(page, size, categoriaId, nombre);
        return productos;
    };

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
}

