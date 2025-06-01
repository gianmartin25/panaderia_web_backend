package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoMovimiento;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.TipoMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tipos-movimiento")
public class TipoMovimientoController {

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @GetMapping
    public ResponseEntity<List<TipoMovimiento>> listarTiposMovimiento() {
        List<TipoMovimiento> tiposMovimiento = tipoMovimientoRepository.findAll();
        return ResponseEntity.ok(tiposMovimiento);
    }
}