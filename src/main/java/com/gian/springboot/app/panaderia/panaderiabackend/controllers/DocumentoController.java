package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Documento;
import com.gian.springboot.app.panaderia.panaderiabackend.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documento")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/registrar")
    public Documento registrarDocumento(@RequestBody Documento documento) {
        return documentoService.registrarDocumento(documento);
    }

    @GetMapping("/{id}")
    public Documento obtenerDocumento(@PathVariable Long id) {
        return documentoService.obtenerDocumento(id);
    }

    @GetMapping
    public List<Documento> listarDocumentos() {
        return documentoService.listarDocumentos();
    }

    @PutMapping("/{id}")
    public Documento actualizarDocumento(@PathVariable Long id, @RequestBody Documento documento) {
        return documentoService.actualizarDocumento(id, documento);
    }

    @DeleteMapping("/{id}")
    public void eliminarDocumento(@PathVariable Long id) {
        documentoService.eliminarDocumento(id);
    }
}