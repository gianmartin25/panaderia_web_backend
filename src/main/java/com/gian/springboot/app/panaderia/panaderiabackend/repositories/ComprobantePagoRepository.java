package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobantePago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    ComprobantePago findComprobantePagoById(Long id);
}