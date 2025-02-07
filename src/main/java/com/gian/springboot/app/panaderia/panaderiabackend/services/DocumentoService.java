package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Documento;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento registrarDocumento(Documento documento) {
        return documentoRepository.save(documento);
    }

    public Documento obtenerDocumento(Long id) {
        Optional<Documento> documento = documentoRepository.findById(id);
        return documento.orElse(null);
    }

    public List<Documento> listarDocumentos() {
        return documentoRepository.findAll();
    }

    public Documento actualizarDocumento(Long id, Documento documento) {
        if (documentoRepository.existsById(id)) {
            documento.setId(id);
            return documentoRepository.save(documento);
        }
        return null;
    }

    public void eliminarDocumento(Long id) {
        documentoRepository.deleteById(id);
    }
}