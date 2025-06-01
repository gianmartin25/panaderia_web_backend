package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u JOIN u.cliente c WHERE LOWER(c.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Usuario> findByNombreOrEmail(@Param("filtro") String filtro);
}