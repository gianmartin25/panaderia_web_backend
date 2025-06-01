package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.EmpleadoDto;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroEmpleadoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.CargoEmpleado;
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
        // Check if the email already exists
        if (empleadoRepository.existsByEmail(registroEmpleadoDto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado en el sistema.");
        }

        // Verify if the employee's position exists
        CargoEmpleado cargoEmpleado = cargoEmpleadoService.obtenerCargoEmpleado(registroEmpleadoDto.getIdCargoEmpleado());
        if (cargoEmpleado == null) {
            throw new IllegalArgumentException("El cargo del empleado no existe");
        }

        // Create and save the Persona
        Persona persona = new Persona();
        persona.setApellidos(registroEmpleadoDto.getApellidos());
        persona.setFechaNacimiento(registroEmpleadoDto.getFechaNacimiento());

        // Create and save the Documento
        Documento documento = new Documento();
        documento.setNumero(registroEmpleadoDto.getDocumento());
        if (tipoDocumentoRepository.existsById(registroEmpleadoDto.getTipoDocumento())) {
            documento.setTipoDocumento(tipoDocumentoRepository.findById(registroEmpleadoDto.getTipoDocumento()).get());
        } else {
            throw new IllegalArgumentException("El tipo de documento no existe");
        }
        persona.setDocumento(documento);
        personaRepository.save(persona);

        // Create and assign the Empleado
        Empleado empleado = new Empleado();
        empleado.setEmail(registroEmpleadoDto.getEmail());
        empleado.setCargoEmpleado(cargoEmpleado);
        empleado.setFechaContratacion(registroEmpleadoDto.getFechaContratacion());
        empleado.setNombres(registroEmpleadoDto.getNombres());
        empleado.setPersona(persona);

        // Save the Empleado in the database
        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        // Map to EmpleadoDto
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setId(empleadoGuardado.getId());
        empleadoDto.setNombres(empleadoGuardado.getNombres());
        empleadoDto.setApellidos(empleadoGuardado.getPersona().getApellidos());
        empleadoDto.setPersonaId(empleadoGuardado.getPersona().getId());
        empleadoDto.setFechaContratacion(empleadoGuardado.getFechaContratacion());
        empleadoDto.setIdCargoEmpleado(empleadoGuardado.getCargoEmpleado().getId());
        empleadoDto.setFechaNacimiento(empleadoGuardado.getPersona().getFechaNacimiento());
        empleadoDto.setCargoEmpleado(empleadoGuardado.getCargoEmpleado().getNombre());

        Documento documentoGuardado = documentoRepository.findDocumentoById(empleadoGuardado.getPersona().getId());
        if (documentoGuardado != null) {
            empleadoDto.setTipoDocumento(documentoGuardado.getTipoDocumento().getNombre());
            empleadoDto.setDocumento(documentoGuardado.getNumero());
        }
        empleadoDto.setEliminado(empleadoGuardado.getEliminado());

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
            empleadoDTO.setApellidos(empleado.getPersona().getApellidos()); // Access Persona's apellidos
            empleadoDTO.setPersonaId(empleado.getPersona().getId()); // Access Persona's ID
            empleadoDTO.setFechaContratacion(empleado.getFechaContratacion());
            empleadoDTO.setEliminado(empleado.getEliminado());
            empleadoDTO.setIdCargoEmpleado(empleado.getCargoEmpleado().getId());
            empleadoDTO.setFechaNacimiento(empleado.getPersona().getFechaNacimiento()); // Access Persona's fechaNacimiento
            empleadoDTO.setCargoEmpleado(cargoEmpleadoService.obtenerCargoEmpleado(empleado.getCargoEmpleado().getId()).getNombre());
            empleadoDTO.setEmail(empleado.getEmail());

            Documento documento = documentoRepository.findDocumentoById(empleado.getPersona().getId());
            if (documento != null) {
                empleadoDTO.setTipoDocumento(documento.getTipoDocumento().getNombre());
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