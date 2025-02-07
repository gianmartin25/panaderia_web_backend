package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/crear-productos")
public class ProductoSeed {

    @Autowired
    private ProductoService productoService;


    @GetMapping("")
    public ResponseEntity<Void> crearOnceProductos() throws IOException {
        productoService.crearProductos();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
