package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.DireccionEntregaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroDireccionDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.DireccionEntrega;
import com.gian.springboot.app.panaderia.panaderiabackend.services.DireccionEntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones-entrega")
public class DireccionEntregaController {

    @Autowired
    private DireccionEntregaService direccionEntregaService;
//    @GetMapping
//    public List<DireccionEntrega> obtenerTodasLasDireccionesEntrega() {
//        return direccionEntregaService.obtenerTodasLasDireccionesEntrega();
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<DireccionEntrega> obtenerDireccionEntregaPorId(@PathVariable Long id) {
//        DireccionEntrega direccionEntrega = direccionEntregaService.obtenerDireccionEntregaPorId(id);
//        if (direccionEntrega != null) {
//            return ResponseEntity.ok(direccionEntrega);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping
    public DireccionEntregaDTO crearDireccionEntrega(@RequestBody RegistroDireccionDTO direccionEntregaDto) {
        return direccionEntregaService.crearDireccionEntrega(direccionEntregaDto);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<DireccionEntrega> actualizarDireccionEntrega(@PathVariable Long id, @RequestBody DireccionEntrega detallesDireccionEntrega) {
//        DireccionEntrega direccionEntregaActualizada = direccionEntregaService.actualizarDireccionEntrega(id, detallesDireccionEntrega);
//        if (direccionEntregaActualizada != null) {
//            return ResponseEntity.ok(direccionEntregaActualizada);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminarDireccionEntrega(@PathVariable Long id) {
//        boolean esEliminado = direccionEntregaService.eliminarDireccionEntrega(id);
//        if (esEliminado) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}