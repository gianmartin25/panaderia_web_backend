package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.models.MetodoPago;
import com.gian.springboot.app.panaderia.panaderiabackend.models.Pago;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.MetodoPagoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.PagoTarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PagoTarjetaRepository pagoTarjetaRepository;
    private final MetodoPagoRepository metodoPagoRepository;

    @Autowired
    public PagoService(PagoRepository pagoRepository, PagoTarjetaRepository pagoTarjetaRepository, MetodoPagoRepository metodoPagoRepository) {
        this.pagoRepository = pagoRepository;
        this.pagoTarjetaRepository = pagoTarjetaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
    }

//    public Pago savePago() {
//        Pago pago = new Pago();
//        MetodoPago metodoPago = metodoPagoRepository.findById(1L).orElseThrow(() -> new RuntimeException("Metodo de pago no encontrado"));
//        pago.
//        return pagoRepository.save(pago);
//    }
}