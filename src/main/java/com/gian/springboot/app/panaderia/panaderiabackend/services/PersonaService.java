package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Persona;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public Persona registrarPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public Persona obtenerPersona(Long id) {
        Optional<Persona> persona = personaRepository.findById(id);
        return persona.orElse(null);
    }

    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    public Persona actualizarPersona(Long id, Persona persona) {
        if (personaRepository.existsById(id)) {
            persona.setId(id);
            return personaRepository.save(persona);
        }
        return null;
    }

    public void eliminarPersona(Long id) {
        personaRepository.deleteById(id);
    }
}