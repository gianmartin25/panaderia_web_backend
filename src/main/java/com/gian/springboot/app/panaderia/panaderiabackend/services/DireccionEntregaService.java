package com.gian.springboot.app.panaderia.panaderiabackend.services;


import com.gian.springboot.app.panaderia.panaderiabackend.dtos.DireccionEntregaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroDireccionDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Cliente;
import com.gian.springboot.app.panaderia.panaderiabackend.models.DireccionEntrega;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ClienteRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.DireccionEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public DireccionEntregaDTO crearDireccionEntrega(RegistroDireccionDTO direccionEntregaDto) {
        Cliente cliente = clienteRepository.findById(direccionEntregaDto.getClienteId()).orElse(null);

        DireccionEntrega direccionEntrega = new DireccionEntrega();
        direccionEntrega.setNombreReceptor(direccionEntregaDto.getNombre());
        direccionEntrega.setDireccion(direccionEntregaDto.getDireccion());
        direccionEntrega.setCiudad(direccionEntregaDto.getCiudad());
        direccionEntrega.setNumeroDni(direccionEntregaDto.getNumeroDni());
        direccionEntrega.setCodigoPostal(Long.parseLong(direccionEntregaDto.getCodigoPostal()));
        direccionEntrega.setCliente(cliente);

        DireccionEntrega direccionEntregaGuardada = direccionEntregaRepository.save(direccionEntrega);
        DireccionEntregaDTO direccionEntregaDTO = new DireccionEntregaDTO();
        direccionEntregaDTO.setId(direccionEntregaGuardada.getId());
        direccionEntregaDTO.setNombre(direccionEntregaGuardada.getNombreReceptor());
        direccionEntregaDTO.setDireccion(direccionEntregaGuardada.getDireccion());
        direccionEntregaDTO.setCiudad(direccionEntregaGuardada.getCiudad());
        direccionEntregaDTO.setCodigoPostal(String.valueOf(direccionEntregaGuardada.getCodigoPostal()));
        direccionEntregaDTO.setClienteId(direccionEntregaGuardada.getCliente().getId());
        return direccionEntregaDTO;
    }
//
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