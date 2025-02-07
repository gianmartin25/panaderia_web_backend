package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Pago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.PagoTarjeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestorPagosService {
    private final PagoService pagoService;
    private final PagoTarjetaService pagoTarjetaService;

    @Autowired
    public GestorPagosService(PagoService pagoService, PagoTarjetaService pagoTarjetaService) {
        this.pagoService = pagoService;
        this.pagoTarjetaService = pagoTarjetaService;
    }

    @Transactional
    public void savePagoAndPagoTarjeta(Pago pago, PagoTarjeta pagoTarjeta) {
        pagoService.savePago(pago);
        pagoTarjetaService.savePagoTarjeta(pagoTarjeta);
    }
}
