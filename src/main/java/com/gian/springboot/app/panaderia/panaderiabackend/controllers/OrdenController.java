package com.gian.springboot.app.panaderia.panaderiabackend.controllers;


import com.gian.springboot.app.panaderia.panaderiabackend.dtos.OrdenResponseDto;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroOrdenDto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.EstadoOrden;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Orden;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.EstadoOrdenRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.services.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private EstadoOrdenRepository estadoOrdenRepository;

    @GetMapping
    public List<OrdenResponseDto> obtenerTodasLasOrdenes() {
        return ordenService.obtenerTodasLasOrdenes();
    }

    @GetMapping("/cliente/{id}")
    public List<OrdenResponseDto> obtenerOrdenesPorCliente(@PathVariable Long id) {
        return ordenService.obtenerOrdenesPorCliente(id);
    }

    @GetMapping("/estados")
    public List<EstadoOrden> obtenerEstadosOrden() {
        return estadoOrdenRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtenerOrdenPorId(@PathVariable Long id) {
        Orden orden = ordenService.obtenerOrdenPorId(id);
        if (orden != null) {
            return ResponseEntity.ok(orden);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado/{estadoId}")
    public ResponseEntity<Void> actualizarEstadoOrden(@PathVariable Long id,
                                                      @PathVariable Long estadoId) {
        boolean actualizado = ordenService.actualizarEstadoOrden(id, estadoId);
        if (actualizado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public OrdenResponseDto crearOrden(@RequestBody RegistroOrdenDto orden) {
        return ordenService.crearOrden(orden);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizarOrden(@PathVariable Long id, @RequestBody Orden detallesOrden) {
        Orden ordenActualizada = ordenService.actualizarOrden(id, detallesOrden);
        if (ordenActualizada != null) {
            return ResponseEntity.ok(ordenActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        boolean esEliminado = ordenService.eliminarOrden(id);
        if (esEliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}