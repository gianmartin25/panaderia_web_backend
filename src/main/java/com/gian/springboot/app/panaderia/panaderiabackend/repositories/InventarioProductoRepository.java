package com.gian.springboot.app.panaderia.panaderiabackend.repositories;

import com.gian.springboot.app.panaderia.panaderiabackend.models.InventarioProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioProductoRepository extends JpaRepository<InventarioProducto, Long> {

    @Query("SELECT SUM(ip.cantidad) FROM InventarioProducto ip WHERE ip.producto.id = :productoId")
    int sumCantidadByProductoId(Long productoId);
}