package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.InventarioProducto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.TipoMovimiento;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.InventarioProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.TipoMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioProductoService {

    @Autowired
    private InventarioProductoRepository inventarioProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;


    @Transactional
    public InventarioProductoDTO registrarMovimientoInventario(RegistroInventarioProductoDTO inventarioProductoDTO) {
        // Fetch the product
        Producto producto = productoRepository.findById(inventarioProductoDTO.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Fetch the movement type
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(inventarioProductoDTO.getTipoMovimientoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de movimiento no encontrado"));

        // Adjust stock based on movement type
        if (tipoMovimiento.getNombre().equalsIgnoreCase("INGRESO")) {
            producto.setTotalCantidad(producto.getTotalCantidad() + inventarioProductoDTO.getCantidad());
        } else if (tipoMovimiento.getNombre().equalsIgnoreCase("SALIDA")) {
            if (producto.getTotalCantidad() < inventarioProductoDTO.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para realizar la salida");
            }
            producto.setTotalCantidad(producto.getTotalCantidad() - inventarioProductoDTO.getCantidad());
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido");
        }

        // Save the updated product
        productoRepository.save(producto);

        // Create and save the inventory record
        InventarioProducto inventario = new InventarioProducto();
        inventario.setProducto(producto);
        inventario.setCantidad(inventarioProductoDTO.getCantidad());
        inventario.setTipoMovimiento(tipoMovimiento);
        InventarioProducto savedInventario = inventarioProductoRepository.save(inventario);

        // Map to DTO and return
        InventarioProductoDTO savedInventarioDTO = new InventarioProductoDTO();
        savedInventarioDTO.setId(savedInventario.getId());
        savedInventarioDTO.setCantidad(savedInventario.getCantidad());
        savedInventarioDTO.setFechaRegistro(savedInventario.getFechaRegistro());
        savedInventarioDTO.setProductoId(savedInventario.getProducto().getId());
        savedInventarioDTO.setProductoName(savedInventario.getProducto().getNombre());
        savedInventarioDTO.setTipoMovimientoId(savedInventario.getTipoMovimiento().getId());
        savedInventarioDTO.setTipoMovimientoNombre(savedInventario.getTipoMovimiento().getNombre());
        return savedInventarioDTO;
    }

    public List<InventarioProductoDTO> listarInventario() {
        List<InventarioProducto> inventario = inventarioProductoRepository.findAll();
        return inventario.stream().map(inventarioProducto -> {
            InventarioProductoDTO inventarioProductoDTO = new InventarioProductoDTO();
            inventarioProductoDTO.setId(inventarioProducto.getId());
            inventarioProductoDTO.setCantidad(inventarioProducto.getCantidad());
            inventarioProductoDTO.setFechaRegistro(inventarioProducto.getFechaRegistro());
            inventarioProductoDTO.setProductoId(inventarioProducto.getProducto().getId());
            inventarioProductoDTO.setProductoName(inventarioProducto.getProducto().getNombre());
            inventarioProductoDTO.setTipoMovimientoId(inventarioProducto.getTipoMovimiento().getId());
            inventarioProductoDTO.setTipoMovimientoNombre(inventarioProducto.getTipoMovimiento().getNombre());
            return inventarioProductoDTO;
        }).toList();
    }

    private void actualizarTotalCantidad(Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        int totalCantidad = inventarioProductoRepository.sumCantidadByProductoId(productoId);
        producto.setTotalCantidad(totalCantidad);
        productoRepository.save(producto);
    }
}