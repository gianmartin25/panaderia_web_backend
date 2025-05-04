package com.gian.springboot.app.panaderia.panaderiabackend.services;

import com.gian.springboot.app.panaderia.panaderiabackend.interfaces.ComprobanteDePagoGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacturaGeneratorTest {

    @Mock
    private TemplateRendererService templateRenderer;

    @InjectMocks
    private FacturaGenerator facturaGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerarComprobante_Success() throws Exception {
        Map<String, Object> datos = new HashMap<>();
        datos.put("key", "value");

        byte[] expectedPdf = new byte[]{1, 2, 3};
        when(templateRenderer.renderToPdf("factura", datos)).thenReturn(expectedPdf);

        byte[] result = facturaGenerator.generarComprobante(datos);

        assertNotNull(result);
        assertArrayEquals(expectedPdf, result);
        verify(templateRenderer, times(1)).renderToPdf("factura", datos);
    }

    @Test
    void testGenerarComprobante_Exception() throws Exception {
        Map<String, Object> datos = new HashMap<>();
        datos.put("key", "value");

        when(templateRenderer.renderToPdf("factura", datos)).thenThrow(new Exception("Rendering error"));

        Exception exception = assertThrows(Exception.class, () -> {
            facturaGenerator.generarComprobante(datos);
        });

        assertEquals("Rendering error", exception.getMessage());
        verify(templateRenderer, times(1)).renderToPdf("factura", datos);
    }
}