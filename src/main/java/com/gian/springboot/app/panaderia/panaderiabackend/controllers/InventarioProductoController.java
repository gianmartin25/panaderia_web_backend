package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.InventarioProducto;
import com.gian.springboot.app.panaderia.panaderiabackend.services.InventarioProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class InventarioProductoController {

    @Autowired
    private InventarioProductoService inventarioProductoService;

    @PostMapping("/agregar")
    public ResponseEntity<InventarioProductoDTO> agregarInventario(@RequestBody RegistroInventarioProductoDTO inventarioProducto) {
        InventarioProductoDTO savedInventario = inventarioProductoService.agregarInventario(inventarioProducto);
        return ResponseEntity.ok(savedInventario);
    }

    @GetMapping
    public List<InventarioProductoDTO> listarInventario() {
        List<InventarioProductoDTO> inventario = inventarioProductoService.listarInventario();
        return inventario;
    }
}