package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.PagoTarjeta;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoTarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoTarjetaService {

    private final PagoTarjetaRepository pagoTarjetaRepository;

    @Autowired
    public PagoTarjetaService(PagoTarjetaRepository pagoTarjetaRepository) {
        this.pagoTarjetaRepository = pagoTarjetaRepository;
    }

    public PagoTarjeta savePagoTarjeta(PagoTarjeta pagoTarjeta) {
        return pagoTarjetaRepository.save(pagoTarjeta);
    }
}