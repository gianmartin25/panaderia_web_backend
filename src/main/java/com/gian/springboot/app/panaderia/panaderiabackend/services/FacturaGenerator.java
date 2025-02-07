package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.interfaces.ComprobanteDePagoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FacturaGenerator implements ComprobanteDePagoGenerator {

    private final TemplateRendererService templateRenderer;

    @Autowired
    public FacturaGenerator(TemplateRendererService templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    public byte[] generarComprobante(Map<String, Object> datos) throws Exception {
        return templateRenderer.renderToPdf("factura", datos);
    }
}