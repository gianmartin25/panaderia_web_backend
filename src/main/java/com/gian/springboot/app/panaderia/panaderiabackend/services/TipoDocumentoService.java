package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoDocumento;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDocumentoService {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumento registrarTipoDocumento(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    public TipoDocumento obtenerTipoDocumento(Long id) {
        Optional<TipoDocumento> tipoDocumento = tipoDocumentoRepository.findById(id);
        return tipoDocumento.orElse(null);
    }

    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoRepository.findAll();
    }

    public TipoDocumento actualizarTipoDocumento(Long id, TipoDocumento tipoDocumento) {
        if (tipoDocumentoRepository.existsById(id)) {
            tipoDocumento.setId(id);
            return tipoDocumentoRepository.save(tipoDocumento);
        }
        return null;
    }

    public void eliminarTipoDocumento(Long id) {
        tipoDocumentoRepository.deleteById(id);
    }
}