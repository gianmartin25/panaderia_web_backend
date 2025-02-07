package com.gian.springboot.app.panaderia.panaderiabackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class TemplateRendererService {

    private final TemplateEngine templateEngine;



    @Autowired
    public TemplateRendererService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] renderToPdf(String templateName, Map<String, Object> datos) throws Exception {
        Context context = new Context();
        context.setVariables(datos);

        // Process the HTML using Thymeleaf
        String html = templateEngine.process(templateName, context);

        // Convert HTML to PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        }
    }
}