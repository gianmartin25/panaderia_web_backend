package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmailAndTipoUsuario(String email, String tipoUsuario);

    Usuario findByEmail(String email);

    Usuario findByEmailOrUsername(String email, String username);

}