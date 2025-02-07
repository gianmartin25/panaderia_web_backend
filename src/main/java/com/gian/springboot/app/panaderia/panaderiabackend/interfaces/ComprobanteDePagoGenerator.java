package com.gian.springboot.app.panaderia.panaderiabackend.interfaces;

import java.util.Map;

// Interface for Payment Vouchers
public interface ComprobanteDePagoGenerator {
    byte[] generarComprobante(Map<String, Object> datos) throws Exception;
}