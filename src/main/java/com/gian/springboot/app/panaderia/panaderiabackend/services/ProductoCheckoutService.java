package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoCartRequestDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoCartResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Producto;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoCheckoutService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<ProductoCartResponseDTO> verificarDisponibilidadProductos(List<ProductoCartRequestDTO> productos) {
        List<ProductoCartResponseDTO> productoCartResponseDTOs = new ArrayList<ProductoCartResponseDTO>();
        for (ProductoCartRequestDTO producto : productos) {
            Producto productoDB = productoRepository.findById(producto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("El producto con ID " + producto.getIdProducto() + " no existe."));
            ProductoCartResponseDTO productoCartResponseDTO = new ProductoCartResponseDTO();
            productoCartResponseDTO.setIdProducto(productoDB.getId());
            productoCartResponseDTO.setNombre(productoDB.getNombre());
            productoCartResponseDTO.setCantidad(producto.getCantidad());
            productoCartResponseDTO.setDescripcion(productoDB.getDescripcion());
            productoCartResponseDTO.setPrecio(productoDB.getPrecio().longValue() * 100);
            productoCartResponseDTO.setImagenUrl(productoDB.getImageUrl());
            if (productoDB.getTotalCantidad() < producto.getCantidad()) {
                throw new RuntimeException("El prodxxucto con ID " + producto.getIdProducto() + "no tiene suficiente cantidad disponible.");
            }
            productoCartResponseDTOs.add(productoCartResponseDTO);
        }
        return productoCartResponseDTOs;
    }


}

