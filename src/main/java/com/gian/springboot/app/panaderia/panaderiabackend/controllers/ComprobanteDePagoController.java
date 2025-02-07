package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobantePago;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ComprobanteDePagoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Controller Layer
@RestController
@RequestMapping("comprobantes")
public class ComprobanteDePagoController {
    private static final Logger logger = LoggerFactory.getLogger(ComprobanteDePagoController.class);
    private final ComprobanteDePagoService comprobanteDePagoService;

    @Autowired
    public ComprobanteDePagoController(ComprobanteDePagoService comprobanteDePagoService) {
        this.comprobanteDePagoService = comprobanteDePagoService;
    }

    @GetMapping("/factura/{id}")
    public ResponseEntity<byte[]> generarFactura(@PathVariable Long id) {
        try {
            byte[] pdf = comprobanteDePagoService.generarFactura(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("factura", "factura.pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/boleta/{id}")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable Long id) {
        try {
            byte[] pdf = comprobanteDePagoService.generarBoleta(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("boleta", "boleta.pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error generating boleta", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}