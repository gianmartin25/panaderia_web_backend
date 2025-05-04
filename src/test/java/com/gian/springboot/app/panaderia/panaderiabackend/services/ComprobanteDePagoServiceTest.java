package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ComprobantePagoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.*;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComprobanteDePagoServiceTest {

    @Mock
    private FacturaGenerator facturaGenerator;

    @Mock
    private BoletaGenerator boletaGenerator;

    @Mock
    private ComprobantePagoRepository comprobantePagoRepository;

    @Mock
    private OrdenService ordenService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TipoComprobanteRepository tipoComprobanteRepository;

    @Mock
    private DetalleOrdenRepository detalleOrdenRepository;

    @Mock
    private BoletaRepository boletaRepository;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private ComprobanteDePagoService comprobanteDePagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardar_Success() {
        ComprobantePagoDTO comprobantePagoDto = new ComprobantePagoDTO();
        comprobantePagoDto.setIdOrden(1L);
        comprobantePagoDto.setMonto(BigDecimal.valueOf(100));

        Orden orden = new Orden();
        orden.setId(1L);
        orden.setPagado(false);
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        TipoCliente tipoCliente = new TipoCliente();
        tipoCliente.setNombre("empresa");
        cliente.setTipoCliente(tipoCliente);
        orden.setCliente(cliente);

        when(ordenService.obtenerOrdenPorId(1L)).thenReturn(orden);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(tipoComprobanteRepository.findById(2L)).thenReturn(Optional.of(new TipoComprobante()));
        when(facturaRepository.obtenerUltimoNumero()).thenReturn(1);
        when(comprobantePagoRepository.save(any(ComprobantePago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ComprobantePago result = comprobanteDePagoService.guardar(comprobantePagoDto);

        assertNotNull(result);
        assertEquals(orden, result.getIdOrden());
        assertEquals(BigDecimal.valueOf(100), result.getMonto());
        assertEquals("F001", result.getSerie());
        verify(facturaRepository, times(1)).save(any(Factura.class));
        verify(comprobantePagoRepository, times(1)).save(any(ComprobantePago.class));
    }

    @Test
    void testGuardar_OrdenNoEncontrada() {
        ComprobantePagoDTO comprobantePagoDto = new ComprobantePagoDTO();
        comprobantePagoDto.setIdOrden(1L);

        when(ordenService.obtenerOrdenPorId(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            comprobanteDePagoService.guardar(comprobantePagoDto);
        });

        assertEquals("No se encontró la orden con id 1", exception.getMessage());
    }

    @Test
    void testGuardar_ClienteNoEncontrado() {
        ComprobantePagoDTO comprobantePagoDto = new ComprobantePagoDTO();
        comprobantePagoDto.setIdOrden(1L);

        Orden orden = new Orden();
        orden.setId(1L);
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        orden.setCliente(cliente);

        when(ordenService.obtenerOrdenPorId(1L)).thenReturn(orden);
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            comprobanteDePagoService.guardar(comprobantePagoDto);
        });

        assertEquals("No se encontró el cliente con id 1", exception.getMessage());
    }

}