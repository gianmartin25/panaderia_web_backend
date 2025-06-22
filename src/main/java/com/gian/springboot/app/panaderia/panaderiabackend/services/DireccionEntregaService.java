package com.gian.springboot.app.panaderia.panaderiabackend.services;


import com.gian.springboot.app.panaderia.panaderiabackend.dtos.DireccionEntregaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroDireccionDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.DireccionEntrega;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ClienteRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.DireccionEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public  class DireccionEntregaService {

    @Autowired
    private DireccionEntregaRepository direccionEntregaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

//    @Override
//    public List<DireccionEntrega> obtenerTodasLasDireccionesEntrega() {
//        return direccionEntregaRepository.findAll();
//    }
//
//    @Override
//    public DireccionEntrega obtenerDireccionEntregaPorId(Long id) {
//        Optional<DireccionEntrega> direccionEntrega = direccionEntregaRepository.findById(id);
//        return direccionEntrega.orElse(null);
//    }

    public DireccionEntregaDTO crearDireccionEntrega(RegistroDireccionDTO registroDireccionDTO) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(registroDireccionDTO.getClienteId());
        if (clienteOptional.isEmpty()) {
            throw new IllegalArgumentException("Cliente not found");
        }

        if (registroDireccionDTO.getCodigoPostal() == null) {
            throw new IllegalArgumentException("Codigo Postal cannot be null");
        }

        DireccionEntrega direccionEntrega = new DireccionEntrega();
        direccionEntrega.setNombreReceptor(registroDireccionDTO.getNombre());
        direccionEntrega.setDireccion(registroDireccionDTO.getDireccion());
        direccionEntrega.setCiudad(registroDireccionDTO.getCiudad());
        direccionEntrega.setNumeroDni(registroDireccionDTO.getNumeroDni());
        direccionEntrega.setCodigoPostal(Long.parseLong(registroDireccionDTO.getCodigoPostal()));
        direccionEntrega.setCliente(clienteOptional.get());

        DireccionEntrega savedDireccion = direccionEntregaRepository.save(direccionEntrega);

        DireccionEntregaDTO direccionEntregaDTO = new DireccionEntregaDTO();
        direccionEntregaDTO.setId(savedDireccion.getId());
        direccionEntregaDTO.setNombre(savedDireccion.getNombreReceptor());
        direccionEntregaDTO.setDireccion(savedDireccion.getDireccion());
        direccionEntregaDTO.setCiudad(savedDireccion.getCiudad());
        direccionEntregaDTO.setCodigoPostal(String.valueOf(savedDireccion.getCodigoPostal()));
        direccionEntregaDTO.setClienteId(savedDireccion.getCliente().getId());

        return direccionEntregaDTO;
    }
//    @Override
//    public DireccionEntrega actualizarDireccionEntrega(Long id, DireccionEntrega detallesDireccionEntrega) {
//        Optional<DireccionEntrega> direccionEntregaOptional = direccionEntregaRepository.findById(id);
//        if (direccionEntregaOptional.isPresent()) {
//            DireccionEntrega direccionEntrega = direccionEntregaOptional.get();
//            direccionEntrega.setCampo1(detallesDireccionEntrega.getCampo1()); // Replace with actual fields
//            direccionEntrega.setCampo2(detallesDireccionEntrega.getCampo2()); // Replace with actual fields
//            return direccionEntregaRepository.save(direccionEntrega);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean eliminarDireccionEntrega(Long id) {
//        if (direccionEntregaRepository.existsById(id)) {
//            direccionEntregaRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }
//    }
}