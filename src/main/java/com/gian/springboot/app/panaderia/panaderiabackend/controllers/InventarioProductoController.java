package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.services.InventarioProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class InventarioProductoController {

    @Autowired
    private InventarioProductoService inventarioProductoService;

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarInventario(@RequestBody RegistroInventarioProductoDTO inventarioProducto) {
        try {
            InventarioProductoDTO savedInventario = inventarioProductoService.registrarMovimientoInventario(inventarioProducto);
            return ResponseEntity.ok(savedInventario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping
    public ResponseEntity<?> listarInventario() {
        try {
            List<InventarioProductoDTO> inventario = inventarioProductoService.listarInventario();
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}