package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.PagoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobantePago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.MetodoPago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.PagoTarjeta;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.MetodoPagoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoTarjetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestorPagosServiceTest {

    @Mock
    private PagoService pagoService;

    @Mock
    private PagoTarjetaService pagoTarjetaService;

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @Mock
    private PagoTarjetaRepository pagoTarjetaRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private GestorPagosService gestorPagosService;

    private PagoDTO pagoDTO;
    private ComprobantePago comprobantePago;
    private MetodoPago metodoPago;

    @BeforeEach
    void setUp() {
        pagoDTO = new PagoDTO();
        pagoDTO.setMetodoPagoId(1L);
        pagoDTO.setMonto(BigDecimal.valueOf(100));
        pagoDTO.setMoneda("USD");
        pagoDTO.setNumeroTarjeta("1234567890123456");
        pagoDTO.setMarca("VISA");
        pagoDTO.setFechaExpiracion("12/25");
        pagoDTO.setTitular("Juan Perez");

        comprobantePago = new ComprobantePago();
        metodoPago = new MetodoPago();
    }

    @Test
    void testSavePagoAndPagoTarjeta_Success() {
        when(metodoPagoRepository.findById(pagoDTO.getMetodoPagoId())).thenReturn(Optional.of(metodoPago));

        gestorPagosService.savePagoAndPagoTarjeta(pagoDTO, comprobantePago);

        verify(pagoTarjetaRepository, times(1)).save(any(PagoTarjeta.class));
    }

    @Test
    void testSavePagoAndPagoTarjeta_MetodoPagoNotFound() {
        when(metodoPagoRepository.findById(pagoDTO.getMetodoPagoId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            gestorPagosService.savePagoAndPagoTarjeta(pagoDTO, comprobantePago);
        });

        assertEquals("Metodo de pago no encontrado", exception.getMessage());
    }
}