package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoOrdenRepository extends JpaRepository<EstadoOrden, Long> {
    Optional<EstadoOrden> findByNombre(String nombre);
}