package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioEmpresaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioPersonaDTO;
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

//    @Autowired
//    private EmpresaRepository empresaRepository;

    @Autowired
    PasswordEncryptionService passwordEncryptionService;
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private TipoClienteRepository tipoClienteRepository;

    public Usuario crearUsuarioEmpresa(RegistroUsuarioEmpresaDTO registroUsuarioEmpresaDTO) {
        Empresa empresa = new Empresa();
        empresa.setRazonSocial(registroUsuarioEmpresaDTO.getRazonSocial());
        empresa.setEliminado(false);

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

    @Transactional
    public Usuario crearUsuarioPersona(RegistroUsuarioPersonaDTO registroUsuarioPersonaDTO) {
        Documento documento = new Documento();
        tipoDocumentoRepository.findById(1L).ifPresent(documento::setTipoDocumento);
        documento.setNumero(registroUsuarioPersonaDTO.getNumeroDocumento());

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
}