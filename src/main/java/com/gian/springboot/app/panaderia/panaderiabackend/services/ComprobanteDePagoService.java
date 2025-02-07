package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.BoletaDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ComprobantePagoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComprobanteDePagoService {

    private final FacturaGenerator facturaGenerator;
    private final BoletaGenerator boletaGenerator;
    private final ComprobantePagoRepository comprobantePagoRepository;
    private final OrdenService ordenService;
    private final ClienteRepository clienteRepository;
    private final TipoComprobanteRepository tipoComprobanteRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;
    private final BoletaRepository boletaRepository;
    private final FacturaRepository facturaRepository;
    private final OrdenRepository ordenRepository;

    @Autowired
    public ComprobanteDePagoService(FacturaGenerator facturaGenerator,
                                    BoletaGenerator boletaGenerator,
                                    ComprobantePagoRepository comprobantePagoRepository,
                                    OrdenService ordenService, ClienteRepository clienteRepository, TipoComprobanteRepository tipoComprobanteRepository, DetalleOrdenRepository detalleOrdenRepository,
                                    BoletaRepository boletaRepository, FacturaRepository facturaRepository, OrdenRepository ordenRepository) {
        this.facturaGenerator = facturaGenerator;
        this.boletaGenerator = boletaGenerator;
        this.comprobantePagoRepository = comprobantePagoRepository;
        this.ordenService = ordenService;
        this.clienteRepository = clienteRepository;
        this.tipoComprobanteRepository = tipoComprobanteRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
        this.boletaRepository = boletaRepository;
        this.facturaRepository = facturaRepository;
        this.ordenRepository = ordenRepository;
    }

    @Transactional
    public ComprobantePago guardar(ComprobantePagoDTO comprobantePagoDto) {
        Orden orden = ordenService.obtenerOrdenPorId(comprobantePagoDto.getIdOrden());
        if (orden == null) {
            throw new IllegalArgumentException("No se encontró la orden con id " + comprobantePagoDto.getIdOrden());
        }
        orden.setPagado(true);

        Cliente cliente = clienteRepository.findById(orden.getCliente().getId()).orElse(null);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró el cliente con id " + orden.getCliente().getId());
        }

        TipoCliente tipoCliente = cliente.getTipoCliente();
        if (tipoCliente == null) {
            throw new IllegalArgumentException("No se encontró el tipo de cliente para el cliente con id " + cliente.getId());
        }

        ComprobantePago comprobantePago = new ComprobantePago();
        comprobantePago.setIdOrden(orden);
        comprobantePago.setMonto(comprobantePagoDto.getMonto());

        if (tipoCliente.getNombre().equals("empresa")) {
            TipoComprobante tipoComprobante = tipoComprobanteRepository.findById(2L).orElse(null);
            if (tipoComprobante == null) {
                throw new IllegalArgumentException("No se encontró el tipo de comprobante con id 2");
            }
            comprobantePago.setTipoComprobante(tipoComprobante);
            comprobantePago.setSerie("F001");
            Factura factura = new Factura();
            factura.setFechaVencimiento(LocalDate.now());
            factura.calcularMontoIgv(comprobantePagoDto.getMonto());
            factura.calcularSubTotal(comprobantePagoDto.getMonto());
            factura.setNumero(facturaRepository.obtenerUltimoNumero() + 1);
            factura.setComprobantesPago(comprobantePago);
            facturaRepository.save(factura);
        } else {
            TipoComprobante tipoComprobante = tipoComprobanteRepository.findById(1L).orElse(null);
            if (tipoComprobante == null) {
                throw new IllegalArgumentException("No se encontró el tipo de comprobante con id 1");
            }

            comprobantePago.setTipoComprobante(tipoComprobante);
            comprobantePago.setSerie("B001");
            Boleta boleta = new Boleta();
            boleta.setNumero(boletaRepository.obtenerUltimoNumero() + 1);
            boleta.setComprobantesPago(comprobantePago);
            boletaRepository.save(boleta);
        }

        return comprobantePagoRepository.save(comprobantePago);
    }

    public byte[] generarFactura(Long comprobanteId) throws Exception {
        ComprobantePago comprobantePago = this.comprobantePagoRepository.findComprobantePagoById(comprobanteId);

        if (comprobantePago == null) {
            throw new IllegalArgumentException("No se encontró el comprobante de pago con id " + comprobanteId);
        }

        HashMap<String, Object> facturaDTO = new HashMap<>();
        Factura factura = facturaRepository.findFacturaByComprobantesPagoId(comprobanteId);
        if (factura == null) {
            throw new IllegalArgumentException("No se encontró la boleta con id " + comprobanteId);
        }

        String numeroComprobante = comprobantePago.getSerie() + "-" + String.format("%06d", factura.getNumero());
        facturaDTO.put("numeroComprobante", numeroComprobante);
//        boletaDTO.setNumero(comprobantePago.getNumero());

        Cliente cliente = clienteRepository.findById(comprobantePago.getIdOrden().getCliente().getId()).orElse(null);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró el cliente con id " + comprobantePago.getIdOrden().getCliente().getId());
        }
        Empresa empresa = cliente.getEmpresa().get(0);


        facturaDTO.put("nombreEmpresa", empresa.getRazonSocial());
        facturaDTO.put("direccion", "AV DIRECCION");
        facturaDTO.put("fecha", comprobantePago.getIdOrden().getFecha().toString());
        facturaDTO.put("montoSubtotal", factura.getSubTotal());
        facturaDTO.put("montoTotal", comprobantePago.getMonto());
        facturaDTO.put("igv", factura.getMontoIgv());
        facturaDTO.put("moneda", "PEN");
        facturaDTO.put("rucCliente", empresa.getDocumento().getNumero());
        facturaDTO.put("razonSocialFiscal", "Cremas y Mil Hojas S.A.C");
        facturaDTO.put("direccionFiscal", "AV DIRECCION FISCAL");
        facturaDTO.put("RUCFiscal", "20215276024");
        facturaDTO.put("fechaEmision", comprobantePago.getFechaEmision().toString());
        facturaDTO.put("fechaVencimiento", comprobantePago.getFechaEmision().toString());
        List<DetalleOrden> detalles = detalleOrdenRepository.findAllByOrdenId(comprobantePago.getIdOrden().getId());

        if (detalles == null) {
            throw new IllegalArgumentException("No se encontraron detalles para la orden con id " + comprobantePago.getIdOrden().getId());
        }

        List<ComprobanteProductoDTO> comprobanteProductos = new ArrayList<>();
        for (DetalleOrden detalle : detalles) {
            System.out.println("Detalle: " + detalle.getProducto().getNombre());
            ComprobanteProductoDTO comprobanteProductoDTO = new ComprobanteProductoDTO();
            comprobanteProductoDTO.setDescripcion(detalle.getProducto().getNombre());
            comprobanteProductoDTO.setCantidad(detalle.getCantidad());
            comprobanteProductoDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            comprobanteProductoDTO.setDescuento(BigDecimal.ZERO);
            comprobanteProductoDTO.setImporte(detalle.getSubtotal());
            comprobanteProductos.add(comprobanteProductoDTO);
        }
        facturaDTO.put("productos", comprobanteProductos);

        return facturaGenerator.generarComprobante(facturaDTO);
    }

    public byte[] generarBoleta(Long comprobanteId) throws Exception {
        ComprobantePago comprobantePago = this.comprobantePagoRepository.findComprobantePagoById(comprobanteId);

        if (comprobantePago == null) {
            throw new IllegalArgumentException("No se encontró el comprobante de pago con id " + comprobanteId);
        }

        HashMap<String, Object> boletaDTO = new HashMap<>();
        Boleta boleta = boletaRepository.findBoletaByComprobantesPagoId(comprobanteId);
        if (boleta == null) {
            throw new IllegalArgumentException("No se encontró la boleta con id " + comprobantePago.getId());
        }

        String numeroComprobante = comprobantePago.getSerie() + "-" + String.format("%06d", boleta.getNumero());
        boletaDTO.put("numeroComprobante", numeroComprobante);
//        boletaDTO.setNumero(comprobantePago.getNumero());

        Cliente cliente = clienteRepository.findById(comprobantePago.getIdOrden().getCliente().getId()).orElse(null);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró el cliente con id " + comprobantePago.getIdOrden().getCliente().getId());
        }
        Persona persona = cliente.getPersona().get(0);


        boletaDTO.put("nombreCompleto", cliente.getNombres() + " " + persona.getApellidos());
        boletaDTO.put("direccion", "AV DIRECCION");
        boletaDTO.put("fecha", comprobantePago.getIdOrden().getFecha().toString());
        boletaDTO.put("montoSubtotal", comprobantePago.getMonto());
        boletaDTO.put("montoTotal", comprobantePago.getMonto());
        boletaDTO.put("moneda", "PEN");
        boletaDTO.put("dni", persona.getDocumento().getNumero());
        boletaDTO.put("razonSocialFiscal", "Cremas y Mil Hojas S.A.C");
        boletaDTO.put("direccionFiscal", "AV DIRECCION FISCAL");
        boletaDTO.put("RUCFiscal", "20215276024");
        boletaDTO.put("fechaEmision", comprobantePago.getFechaEmision().toString());
//        boletaDTO.setNombreCompleto(cliente.getNombres() + " " + persona.getApellidos());
//        boletaDTO.setDireccion("AV DIRECCION");
//        boletaDTO.setFecha(comprobantePago.getIdOrden().getFecha().toString());
//        boletaDTO.setMontoTotal(comprobantePago.getMonto());
//        boletaDTO.setDni(persona.getDocumento().getNumero());
//        boletaDTO.setRazonSocialFiscal("Cremas y Mil Hojas S.A.C");
//        boletaDTO.setDireccionFiscal("AV DIRECCION FISCAL");
//        boletaDTO.setFechaEmision(comprobantePago.getFechaEmision().toString());
//        boletaDTO.setFechaEmision(comprobantePago.getFechaEmision().toString());

        List<DetalleOrden> detalles = detalleOrdenRepository.findAllByOrdenId(comprobantePago.getIdOrden().getId());

        if (detalles == null) {
            throw new IllegalArgumentException("No se encontraron detalles para la orden con id " + comprobantePago.getIdOrden().getId());
        }

        List<ComprobanteProductoDTO> comprobanteProductos = new ArrayList<>();
        for (DetalleOrden detalle : detalles) {
            System.out.println("Detalle: " + detalle.getProducto().getNombre());
            ComprobanteProductoDTO comprobanteProductoDTO = new ComprobanteProductoDTO();
            comprobanteProductoDTO.setDescripcion(detalle.getProducto().getNombre());
            comprobanteProductoDTO.setCantidad(detalle.getCantidad());
            comprobanteProductoDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            comprobanteProductoDTO.setDescuento(BigDecimal.ZERO);
            comprobanteProductoDTO.setImporte(detalle.getSubtotal());
            comprobanteProductos.add(comprobanteProductoDTO);
        }
        boletaDTO.put("productos", comprobanteProductos);

        return boletaGenerator.generarComprobante(boletaDTO);
    }
}