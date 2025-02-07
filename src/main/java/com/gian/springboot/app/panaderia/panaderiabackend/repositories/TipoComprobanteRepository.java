package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoComprobanteRepository extends JpaRepository<TipoComprobante, Long> {
    TipoComprobante findTipoComprobanteById(Long tipoComprobanteId);
}
