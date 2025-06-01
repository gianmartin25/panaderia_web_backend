package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioEmpresaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroUsuarioPersonaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioClienteDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.UsuarioResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.exceptions.UsuarioExistenteException;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class UsuarioService {

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
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    public Usuario crearUsuarioEmpresa(RegistroUsuarioEmpresaDTO registroUsuarioEmpresaDTO) {
        Empresa empresa = new Empresa();
        empresa.setRazonSocial(registroUsuarioEmpresaDTO.getRazonSocial());
        empresa.setEliminado(false);

        Cliente clienteExistente = clienteRepository.findByEmail(registroUsuarioEmpresaDTO.getEmail());
        if (clienteExistente != null) {
            throw new UsuarioExistenteException("El cliente con este email ya existe");
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
        cliente.setEmail(registroUsuarioEmpresaDTO.getEmail());
        cliente.getEmpresa().add(empresa);

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario("client");
        usuario.setUsername(registroUsuarioEmpresaDTO.getUsername());
        usuario.setPassword(passwordEncryptionService.encryptPassword(registroUsuarioEmpresaDTO.getPassword()));
        usuario.getCliente().add(cliente);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Generate and save verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsuario(savedUsuario);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours
        verificationTokenRepository.save(verificationToken);

        // Send verification email
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        try {
            emailService.sendVerificationEmail(
                    registroUsuarioEmpresaDTO.getEmail(),
                    "Verifica tu cuenta",
                    verificationLink
            );
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de verificación.");
        }

        return savedUsuario;
    }

    public Usuario crearUsuarioPersona(RegistroUsuarioPersonaDTO registroUsuarioPersonaDTO) {
        Documento documento = new Documento();
        tipoDocumentoRepository.findById(1L).ifPresent(documento::setTipoDocumento);
        documento.setNumero(registroUsuarioPersonaDTO.getNumeroDocumento());

        Cliente clienteExistente = clienteRepository.findByEmail(registroUsuarioPersonaDTO.getEmail());
        if (clienteExistente != null) {
            throw new UsuarioExistenteException("El cliente con este email ya existe");
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
        cliente.setEmail(registroUsuarioPersonaDTO.getEmail());
        cliente.getPersona().add(persona);

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario("client");
        usuario.setUsername(registroUsuarioPersonaDTO.getUsername());
        usuario.setPassword(passwordEncryptionService.encryptPassword(registroUsuarioPersonaDTO.getPassword()));
        usuario.getCliente().add(cliente);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Generate and save verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsuario(savedUsuario);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours
        verificationTokenRepository.save(verificationToken);

        // Send verification email
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        try {
            emailService.sendVerificationEmail(
                    registroUsuarioPersonaDTO.getEmail(),
                    "Verifica tu cuenta",
                    verificationLink
            );
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de verificación.");
        }

        return savedUsuario;
    }

    public List<UsuarioClienteDTO> obtenerClientes() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioClienteDTO> usuarioClienteDTOS = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            List<Cliente> clientes = usuario.getCliente();
            for (Cliente cliente : clientes) {
                UsuarioClienteDTO usuarioClienteDTO = new UsuarioClienteDTO();
                usuarioClienteDTO.setId(usuario.getId().toString());

                if (cliente.getPersona().size() > 0) {
                    Persona persona = cliente.getPersona().get(0);
                    usuarioClienteDTO.setNombres(cliente.getNombres() + " " + persona.getApellidos());
                    usuarioClienteDTO.setEmail(cliente.getEmail());
                    usuarioClienteDTO.setNumeroDocumento(persona.getDocumento().getNumero());
                    usuarioClienteDTO.setTipoDocumento(persona.getDocumento().getTipoDocumento().getNombre());
                    usuarioClienteDTO.setTipoCliente(cliente.getTipoCliente().getNombre());
                } else {
                    Empresa empresa = cliente.getEmpresa().get(0);
                    usuarioClienteDTO.setNombres(empresa.getRazonSocial());
                    usuarioClienteDTO.setEmail(cliente.getEmail());
                    usuarioClienteDTO.setNumeroDocumento(empresa.getDocumento().getNumero());
                    usuarioClienteDTO.setTipoDocumento(empresa.getDocumento().getTipoDocumento().getNombre());
                    usuarioClienteDTO.setTipoCliente(cliente.getTipoCliente().getNombre());
                }
                usuarioClienteDTOS.add(usuarioClienteDTO);
            }
        }
        return usuarioClienteDTOS;
    }

    public boolean eliminarUsuarios() {
        transportistaRepository.deleteAll();
        empleadoRepository.deleteAll();
        personaRepository.deleteAll();
        empresaRepository.deleteAll();
        clienteRepository.deleteAll();
        usuarioRepository.deleteAll();
        return true;
    }


    public UsuarioResponseDTO getUsuarioByEmailAndUsuarioType(String email, String userType) {
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getTipoUsuario().equals(userType) &&
                        u.getCliente().stream().anyMatch(c -> c.getEmail().equals(email)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Map the Usuario to UsuarioResponseDTO
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setUsername(usuario.getUsername());
        usuarioResponseDTO.setTipoUsuario(usuario.getTipoUsuario());

        // Retrieve email and tipoCliente from the first associated Cliente
        List<Cliente> clientes = usuario.getCliente();
        if (!clientes.isEmpty()) {
            Cliente cliente = clientes.get(0);
            usuarioResponseDTO.setEmail(cliente.getEmail());
            usuarioResponseDTO.setTipoCliente(cliente.getTipoCliente().getNombre());
        }

        return usuarioResponseDTO;
    }
}