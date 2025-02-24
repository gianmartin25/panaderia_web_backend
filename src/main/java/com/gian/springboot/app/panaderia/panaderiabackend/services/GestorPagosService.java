package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.PagoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobantePago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.MetodoPago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Pago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.PagoTarjeta;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.MetodoPagoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoTarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestorPagosService {
    private final PagoService pagoService;
    private final PagoTarjetaService pagoTarjetaService;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PagoTarjetaRepository pagoTarjetaRepository;
    private final PagoRepository pagoRepository;

    @Autowired
    public GestorPagosService(PagoService pagoService, PagoTarjetaService pagoTarjetaService, MetodoPagoRepository metodoPagoRepository, PagoTarjetaRepository pagoTarjetaRepository, PagoRepository pagoRepository) {
        this.pagoService = pagoService;
        this.pagoTarjetaService = pagoTarjetaService;
        this.metodoPagoRepository = metodoPagoRepository;
        this.pagoTarjetaRepository = pagoTarjetaRepository;
        this.pagoRepository = pagoRepository;
    }

    @Transactional
    public void savePagoAndPagoTarjeta(PagoDTO pagoDTO, ComprobantePago comprobantePago) {
        MetodoPago metodoPago = metodoPagoRepository.findById(pagoDTO.getMetodoPagoId()).orElseThrow(() -> new RuntimeException("Metodo de pago no encontrado"));
        Pago pago = new Pago();
        pago.setMetodoPago(metodoPago);
        pago.setMonto(pagoDTO.getMonto());
        pago.setComprobante(comprobantePago);
        pago.setMoneda(pagoDTO.getMoneda());

        PagoTarjeta pagoTarjeta = new PagoTarjeta();
        pagoTarjeta.setNumeroTarjeta(pagoDTO.getNumeroTarjeta());
        pagoTarjeta.setMarca(pagoDTO.getMarca());
        pagoTarjeta.setFechaExpiracion(pagoDTO.getFechaExpiracion());
        pagoTarjeta.setPago(pago);
        pagoTarjeta.setTitular(pagoDTO.getTitular());
        pagoTarjeta.setPago(pago);

        pagoTarjetaRepository.save(pagoTarjeta);
    }
}
