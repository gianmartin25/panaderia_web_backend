package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioEmpresaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioPersonaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.exceptions.UsuarioExistenteException;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UsuarioEmpresaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


    @Autowired
    PasswordEncryptionService passwordEncryptionService;
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private TipoClienteRepository tipoClienteRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private TransportistaRepository transportistaRepository;

    public Usuario crearUsuarioEmpresa(RegistroUsuarioEmpresaDTO registroUsuarioEmpresaDTO) {
        Empresa empresa = new Empresa();
        empresa.setRazonSocial(registroUsuarioEmpresaDTO.getRazonSocial());
        empresa.setEliminado(false);

        Usuario existeUsuario = usuarioRepository.findByEmailOrUsername(registroUsuarioEmpresaDTO.getEmail(), registroUsuarioEmpresaDTO.getUsername());
        if (existeUsuario != null) {
            throw new UsuarioExistenteException("El usuario ya existe");
        }

        Documento documento = new Documento();

        tipoDocumentoRepository.findById(3L).ifPresent(documento::setTipoDocumento);
        documento.setNumero(registroUsuarioEmpresaDTO.getNumeroDocumento());
        empresa.setDocumento(documento);

        TipoCliente tipoCliente = tipoClienteRepository.findById(2L).orElseThrow();

        Cliente cliente = new Cliente();
        cliente.setNombres(registroUsuarioEmpresaDTO.getNombreEmpresa());
        cliente.setTipoCliente(tipoCliente);
        cliente.setTelefono(registroUsuarioEmpresaDTO.getTelefono());
        cliente.getEmpresa().add(empresa);

        Usuario usuario = new Usuario();
        usuario.setEmail(registroUsuarioEmpresaDTO.getEmail());
        usuario.setTipoUsuario("client");
        usuario.setUsername(registroUsuarioEmpresaDTO.getUsername());
        usuario.setPassword(passwordEncryptionService.encryptPassword(registroUsuarioEmpresaDTO.getPassword()));
        usuario.getCliente().add(cliente);


        return usuarioRepository.save(usuario);
    }

    public Usuario crearUsuarioPersona(RegistroUsuarioPersonaDTO registroUsuarioPersonaDTO) {
        Documento documento = new Documento();
        tipoDocumentoRepository.findById(1L).ifPresent(documento::setTipoDocumento);
        documento.setNumero(registroUsuarioPersonaDTO.getNumeroDocumento());

        Usuario existeUsuario = usuarioRepository.findByEmailOrUsername(registroUsuarioPersonaDTO.getEmail(), registroUsuarioPersonaDTO.getUsername());
        if (existeUsuario != null) {
            throw new UsuarioExistenteException("El usuario ya existe");
        }
        Persona persona = new Persona();
        persona.setApellidos(registroUsuarioPersonaDTO.getApellidos());
        persona.setFechaNacimiento(registroUsuarioPersonaDTO.getFechaNacimiento());
        persona.setDocumento(documento);

        TipoCliente tipoCliente = tipoClienteRepository.findById(1L).orElseThrow();

        Cliente cliente = new Cliente();
        cliente.setNombres(registroUsuarioPersonaDTO.getNombres());
        cliente.setTipoCliente(tipoCliente);
        cliente.setTelefono(registroUsuarioPersonaDTO.getTelefono());
        cliente.getPersona().add(persona);


        Usuario usuario = new Usuario();
        usuario.setEmail(registroUsuarioPersonaDTO.getEmail());
        usuario.setTipoUsuario("client");
        usuario.setUsername(registroUsuarioPersonaDTO.getUsername());
        usuario.setPassword(passwordEncryptionService.encryptPassword(registroUsuarioPersonaDTO.getPassword()));
        usuario.getCliente().add(cliente);


        return usuarioRepository.save(usuario);
    }

    public boolean eliminarUsuarios(){
        transportistaRepository.deleteAll();
        empleadoRepository.deleteAll();
        personaRepository.deleteAll();
        empresaRepository.deleteAll();
        clienteRepository.deleteAll();
        usuarioRepository.deleteAll();
        return true;
    }
}