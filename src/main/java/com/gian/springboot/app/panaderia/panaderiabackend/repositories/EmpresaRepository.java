package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}