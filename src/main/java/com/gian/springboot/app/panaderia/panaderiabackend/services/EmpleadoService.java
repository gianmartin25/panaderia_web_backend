package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.EmpleadoDto;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroEmpleadoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Documento;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Empleado;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Persona;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.DocumentoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.EmpleadoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PersonaRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private CargoEmpleadoService cargoEmpleadoService;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;
    @Autowired
    private PersonaRepository personaRepository;

    @Transactional
    public EmpleadoDto registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDto) {
        // Verificar si el cargo del empleado existe
        if (cargoEmpleadoService.obtenerCargoEmpleado(registroEmpleadoDto.getIdCargoEmpleado()) == null) {
            throw new IllegalArgumentException("El cargo del empleado no existe");
        }

        // Crear y guardar la persona
        Persona persona = new Persona();
        persona.setApellidos(registroEmpleadoDto.getApellidos());
        persona.setFechaNacimiento(registroEmpleadoDto.getFechaNacimiento());

        // Crear y guardar el documento
        Documento documento = new Documento();
        documento.setNumero(registroEmpleadoDto.getDocumento());
        if (tipoDocumentoRepository.existsById(registroEmpleadoDto.getTipoDocumento())) {
            documento.setTipoDocumento(tipoDocumentoRepository.findById(registroEmpleadoDto.getTipoDocumento()).get());
        } else {
            throw new IllegalArgumentException("El tipo de documento no existe");
        }
        persona.setDocumento(documento);
        personaRepository.save(persona);

        // Crear y asignar el empleado
        Empleado empleado = new Empleado();
        empleado.setIdCargoEmpleado(registroEmpleadoDto.getIdCargoEmpleado());
        empleado.setFechaContratacion(registroEmpleadoDto.getFechaContratacion());
        empleado.setNombres(registroEmpleadoDto.getNombres());
        empleado.setPersona(persona);




        // Guardar el empleado en la base de datos
        Empleado empleadoGuardado = empleadoRepository.save(empleado);
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setId(empleadoGuardado.getId());
        empleadoDto.setNombres(empleadoGuardado.getNombres());
        empleadoDto.setApellidos(empleadoGuardado.getPersonaApellidos());
        empleadoDto.setPersonaId(empleadoGuardado.getPersonaId());
        empleadoDto.setFechaContratacion(empleadoGuardado.getFechaContratacion());
        empleadoDto.setIdCargoEmpleado(empleadoGuardado.getIdCargoEmpleado());
        empleadoDto.setFechaNacimiento(empleadoGuardado.getPersonaFechaNacimiento());
        empleadoDto.setCargoEmpleado(cargoEmpleadoService.obtenerCargoEmpleado(empleadoGuardado.getIdCargoEmpleado()).getNombre());
        Documento documentoGuardado = documentoRepository.findDocumentoById(empleadoGuardado.getPersonaId());
        if (documentoGuardado != null) {
            empleadoDto.setTipoDocumento(documentoGuardado.getTipoDocumento().getNombre());
            empleadoDto.setDocumento(documentoGuardado.getNumero());
        }
        empleadoDto.setEliminado(empleadoGuardado.getEliminado());
        empleadoDto.setDocumento(documento.getNumero());
        empleadoDto.setTipoDocumento(documento.getTipoDocumento().getNombre());

        return empleadoDto;

    }


    public Empleado obtenerEmpleado(Long id) {
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        return empleado.orElse(null);
    }

    public List<EmpleadoDto> listarEmpleados() {
        List<Empleado> empleados = empleadoRepository.findAll();

        return empleados.stream().map(empleado -> {
            EmpleadoDto empleadoDTO = new EmpleadoDto();
            empleadoDTO.setId(empleado.getId());
            empleadoDTO.setNombres(empleado.getNombres());
            empleadoDTO.setApellidos(empleado.getPersonaApellidos());
            empleadoDTO.setPersonaId(empleado.getPersonaId());
            empleadoDTO.setFechaContratacion(empleado.getFechaContratacion());
            empleadoDTO.setEliminado(empleado.getEliminado());
            empleadoDTO.setIdCargoEmpleado(empleado.getIdCargoEmpleado());
            empleadoDTO.setFechaNacimiento(empleado.getPersonaFechaNacimiento());
            empleadoDTO.setCargoEmpleado(cargoEmpleadoService.obtenerCargoEmpleado(empleado.getIdCargoEmpleado()).getNombre());

            // Obtener documentos asociados a la persona del empleado
            Documento documento = documentoRepository.findDocumentoById(empleado.getPersonaId());
            if (documento != null) {
                String tipoDocumento = documento.getTipoDocumento().getNombre();
                empleadoDTO.setTipoDocumento(tipoDocumento);
                empleadoDTO.setDocumento(documento.getNumero());
            }

            return empleadoDTO;
        }).collect(Collectors.toList());
    }

    public Empleado actualizarEmpleado(Long id, Empleado empleado) {
        if (empleadoRepository.existsById(id)) {
            empleado.setId(id);
            return empleadoRepository.save(empleado);
        }
        return null;
    }

    public void eliminarEmpleado(Long id) {
        empleadoRepository.deleteById(id);
    }
}