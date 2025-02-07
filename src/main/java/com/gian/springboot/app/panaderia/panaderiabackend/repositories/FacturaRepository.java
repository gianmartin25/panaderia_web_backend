package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    @Query("SELECT COALESCE(MAX(f.numero), 0) FROM Factura f")
    int obtenerUltimoNumero();

    Factura findFacturaByComprobantesPagoId(Long id);
}
