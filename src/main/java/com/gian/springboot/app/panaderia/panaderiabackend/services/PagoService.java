package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.Pago;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    @Autowired
    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public Pago savePago(Pago pago) {
        return pagoRepository.save(pago);
    }
}