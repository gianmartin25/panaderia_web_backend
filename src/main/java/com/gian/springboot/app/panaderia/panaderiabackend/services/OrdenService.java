package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.*;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    DireccionEntregaRepository direccionEntregaRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    TransportistaRepository transportistaRepository;
    @Autowired
    private ClienteRepository clienteRepository;


    public List<OrdenResponseDto> obtenerTodasLasOrdenes() {
        List<Orden> ordenes = ordenRepository.findAll();
        List<OrdenResponseDto> ordenesResponse = new ArrayList<>();
        for (Orden orden : ordenes) {
            OrdenResponseDto ordenResponseDto = new OrdenResponseDto();
            ordenResponseDto.setId(orden.getId());
            ordenResponseDto.setEstado(orden.getEstado());
            ordenResponseDto.setFechaCreacion(orden.getFecha().toString());
            ordenResponseDto.setTotal(orden.getTotal().toString());
            ordenResponseDto.setPagado(orden.getPagado());
            ordenResponseDto.setCliente(orden.getCliente().getNombres());


            List<ProductoOrdenResponseDTO> productos = new ArrayList<>();
            for (DetalleOrden detalle : orden.getDetalles()) {
                ProductoOrdenResponseDTO productoOrdenResponseDTO = new ProductoOrdenResponseDTO();
                productoOrdenResponseDTO.setId(detalle.getProducto().getId());
                productoOrdenResponseDTO.setNombre(detalle.getProducto().getNombre());
                productoOrdenResponseDTO.setCantidad(detalle.getCantidad());
                productoOrdenResponseDTO.setPrecio(detalle.getPrecioUnitario());
                productoOrdenResponseDTO.setSubTotal(detalle.getSubtotal().toString());
                productoOrdenResponseDTO.setImagen("http://localhost:8080/api/uploads/" + detalle.getProducto().getImageUrl());
                productos.add(productoOrdenResponseDTO);
            }
            ordenResponseDto.setProductos(productos);
            ordenesResponse.add(ordenResponseDto);
        }
        return ordenesResponse;
    }

    public List<OrdenResponseDto> obtenerOrdenesPorCliente(Long id) {
        List<Orden> ordenes = ordenRepository.findAllByClienteId(id);
        List<OrdenResponseDto> ordenesResponse = new ArrayList<>();
        for (Orden orden : ordenes) {
            OrdenResponseDto ordenResponseDto = new OrdenResponseDto();
            ordenResponseDto.setId(orden.getId());
            ordenResponseDto.setEstado(orden.getEstado());
            ordenResponseDto.setFechaCreacion(orden.getFecha().toString());
            ordenResponseDto.setTotal(orden.getTotal().toString());
            ordenResponseDto.setPagado(orden.getPagado());
            List<ProductoOrdenResponseDTO> productos = new ArrayList<>();
            for (DetalleOrden detalle : orden.getDetalles()) {
                ProductoOrdenResponseDTO productoOrdenResponseDTO = new ProductoOrdenResponseDTO();
                productoOrdenResponseDTO.setId(detalle.getProducto().getId());
                productoOrdenResponseDTO.setNombre(detalle.getProducto().getNombre());
                productoOrdenResponseDTO.setCantidad(detalle.getCantidad());
                productoOrdenResponseDTO.setPrecio(detalle.getPrecioUnitario());
                productoOrdenResponseDTO.setSubTotal(detalle.getSubtotal().toString());
                productoOrdenResponseDTO.setImagen("http://localhost:8080/api/uploads/" + detalle.getProducto().getImageUrl());
                productos.add(productoOrdenResponseDTO);
            }
            ordenResponseDto.setProductos(productos);
            ordenesResponse.add(ordenResponseDto);
        }
        return ordenesResponse;
    }

    public Orden obtenerOrdenPorId(Long id) {
        Optional<Orden> orden = ordenRepository.findById(id);
        return orden.orElse(null);
    }

    public OrdenResponseDto crearOrden(RegistroOrdenDto registroOrdenDto) {
        Orden orden = new Orden();

        for (ProductoCartRequestDTO detalleOrden : registroOrdenDto.getProductos()) {
            Optional<Producto> productoOptional = productoRepository.findById(detalleOrden.getIdProducto());
            if (productoOptional.isPresent()) {
                Producto producto = productoOptional.get();
                DetalleOrden detalle = new DetalleOrden();
                detalle.setProducto(producto);
                detalle.setCantidad(detalleOrden.getCantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.calcularSubtotal();
                orden.getDetalles().add(detalle);
            }
        }

        orden.calcularTotal();

        Optional<Transportista> transportista = transportistaRepository.findById(1L);

        if (transportista.isPresent()) {
            orden.setTransportista(transportista.get());
        }

        Optional<DireccionEntrega> direccionEntrega = direccionEntregaRepository.findById(registroOrdenDto.getIdDireccionEntrega());
        if (direccionEntrega.isPresent()) {
            orden.setDireccionEntrega(direccionEntrega.get());
        }

        Empleado empleado = empleadoRepository.findById(1L).get();
        orden.setEmpleado(empleado);

        Cliente cliente = clienteRepository.findById(registroOrdenDto.getClienteId()).get();
        orden.setCliente(cliente);

        ordenRepository.save(orden);

        OrdenResponseDto ordenResponseDto = new OrdenResponseDto();
        ordenResponseDto.setId(orden.getId());
        ordenResponseDto.setEstado(orden.getEstado());
        ordenResponseDto.setFechaCreacion(orden.getFecha().toString());
        ordenResponseDto.setTotal(orden.getTotal().toString());

        List<ProductoOrdenResponseDTO> productos = new ArrayList<>();
        for (DetalleOrden detalle : orden.getDetalles()) {
            ProductoOrdenResponseDTO productoOrdenResponseDTO = new ProductoOrdenResponseDTO();
            productoOrdenResponseDTO.setId(detalle.getProducto().getId());
            productoOrdenResponseDTO.setNombre(detalle.getProducto().getNombre());
            productoOrdenResponseDTO.setCantidad(detalle.getCantidad());
            productoOrdenResponseDTO.setPrecio(detalle.getPrecioUnitario());
            productoOrdenResponseDTO.setSubTotal(detalle.getSubtotal().toString());
            productos.add(productoOrdenResponseDTO);
        }
        ordenResponseDto.setProductos(productos);
        return ordenResponseDto;
    }

    public Orden actualizarOrden(Long id, Orden detallesOrden) {
        Optional<Orden> ordenOptional = ordenRepository.findById(id);
        if (ordenOptional.isPresent()) {
            Orden orden = ordenOptional.get();
            // Update fields here
            return ordenRepository.save(orden);
        } else {
            return null;
        }
    }

    public boolean eliminarOrden(Long id) {
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}