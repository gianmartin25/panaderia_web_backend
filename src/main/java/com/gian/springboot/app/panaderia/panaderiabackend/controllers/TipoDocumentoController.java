package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoDocumento;
import com.gian.springboot.app.panaderia.panaderiabackend.services.TipoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-documento")
public class TipoDocumentoController {

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @PostMapping("/registrar")
    public TipoDocumento registrarTipoDocumento(@RequestBody TipoDocumento tipoDocumento) {
        return tipoDocumentoService.registrarTipoDocumento(tipoDocumento);
    }

    @GetMapping("/{id}")
    public TipoDocumento obtenerTipoDocumento(@PathVariable Long id) {
        return tipoDocumentoService.obtenerTipoDocumento(id);
    }

    @GetMapping
    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoService.listarTiposDocumento();
    }

    @PutMapping("/{id}")
    public TipoDocumento actualizarTipoDocumento(@PathVariable Long id, @RequestBody TipoDocumento tipoDocumento) {
        return tipoDocumentoService.actualizarTipoDocumento(id, tipoDocumento);
    }

    @DeleteMapping("/{id}")
    public void eliminarTipoDocumento(@PathVariable Long id) {
        tipoDocumentoService.eliminarTipoDocumento(id);
    }
}