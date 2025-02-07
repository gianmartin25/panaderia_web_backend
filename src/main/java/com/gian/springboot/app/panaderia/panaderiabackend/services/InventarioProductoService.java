package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.InventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.RegistroInventarioProductoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.InventarioProducto;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.InventarioProductoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
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

    @Transactional
    public InventarioProductoDTO agregarInventario(RegistroInventarioProductoDTO inventarioProducto) {
        InventarioProducto inventario = new InventarioProducto();
        inventario.setCantidad(inventarioProducto.getCantidad());
        Producto product =  productoRepository.findById(inventarioProducto.getProductoId()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        inventario.setProducto(product);

        InventarioProducto savedInventario = inventarioProductoRepository.save(inventario);
        actualizarTotalCantidad(inventario.getProducto().getId());

        InventarioProductoDTO savedInventarioDTO = new InventarioProductoDTO();
        savedInventarioDTO.setId(savedInventario.getId());
        savedInventarioDTO.setCantidad(savedInventario.getCantidad());
        savedInventarioDTO.setFechaIngreso(savedInventario.getFechaIngreso());
        savedInventarioDTO.setProductoId(savedInventario.getProducto().getId());
        savedInventarioDTO.setProductoName(savedInventario.getProducto().getNombre());
        return savedInventarioDTO;
    }

    public List<InventarioProductoDTO> listarInventario() {
        List<InventarioProducto> inventario = inventarioProductoRepository.findAll();
        return inventario.stream().map(inventarioProducto -> {
            InventarioProductoDTO inventarioProductoDTO = new InventarioProductoDTO();
            inventarioProductoDTO.setId(inventarioProducto.getId());
            inventarioProductoDTO.setCantidad(inventarioProducto.getCantidad());
            inventarioProductoDTO.setFechaIngreso(inventarioProducto.getFechaIngreso());
            inventarioProductoDTO.setProductoId(inventarioProducto.getProducto().getId());
            inventarioProductoDTO.setProductoName(inventarioProducto.getProducto().getNombre());
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