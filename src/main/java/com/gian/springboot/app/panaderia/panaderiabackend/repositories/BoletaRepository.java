package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    @Query("SELECT COALESCE(MAX(b.numero), 0) FROM Boleta b")
    int obtenerUltimoNumero();

    Boleta findBoletaByComprobantesPagoId(Long id);
}
