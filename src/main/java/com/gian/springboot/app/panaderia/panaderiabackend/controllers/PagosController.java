package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ComprobantePagoDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.PagosSesionDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoCartRequestDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.dtos.ProductoCartResponseDTO;
import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobantePago;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ComprobanteDePagoService;
import com.gian.springboot.app.panaderia.panaderiabackend.services.GestorPagosService;
import com.gian.springboot.app.panaderia.panaderiabackend.services.ProductoCheckoutService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/pagos")
public class PagosController {

    @Autowired
    ProductoCheckoutService productoCheckoutService;

    @Autowired
    GestorPagosService gestorPagosService;

    @Autowired
    ComprobanteDePagoService comprobanteDePagoService;

    private static final String STRIPE_WEBHOOK_SECRET = "whsec_VdZuMxYlTokIJV74YsXVzA6d1kMQZGZr";


    public PagosController() {
        // Configura tu clave secreta de Stripe
        Stripe.apiKey = "sk_test_51QORmqG1m4VDCgI6YkS53kyBZ2AtgA2lZRInltx4PjlUrwoaHqXuBmu3l7ZtdWXNWbgYseketQ1R6RdwOfzuPCR000ydGSLvQz";
    }

    @PostMapping("/crear-sesion-pagos")
    public Map<String, String> createCheckoutSession(@RequestBody PagosSesionDTO pagosSesionDTO) {
        try {
            List<ProductoCartRequestDTO> productos = Arrays.asList(pagosSesionDTO.getProductos());

            // Verificar disponibilidad de productos
            List<ProductoCartResponseDTO> productoCartResponseDTOS = productoCheckoutService.verificarDisponibilidadProductos(productos);

            String successUrl = "http://localhost:4200/success";
            String cancelUrl = "http://localhost:4200/cancel";

            List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

            for (ProductoCartResponseDTO producto : productoCartResponseDTOS) {

                SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("pen")
                                        .setUnitAmount(producto.getPrecio().longValue()) // Configura el precio real aquí
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("Producto ID: " + producto.getIdProducto())
                                                        .setDescription(producto.getDescripcion())
                                                        .setName(producto.getNombre())
                                                        .addAllImage(Arrays.asList("https://hkdk.events/8mhn0xwd3zvi30/" + producto.getImagenUrl()))
                                                        .build()
                                        )
                                        .build()
                        )
                        .setQuantity(producto.getCantidad().longValue())
                        .build();

                lineItems.add(lineItem);
            }
            SessionCreateParams params = SessionCreateParams.builder()
                    .putMetadata("orden_id", pagosSesionDTO.getOrdenId().toString())
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addAllLineItem(lineItems)
                    .build();

            // Crear la sesión de Checkout
            Session session = Session.create(params);

            // Retornar la URL de la sesión
            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error creando la sesión de Checkout", e);
        }
    }


    @PostMapping("/webhook")
    public Event handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Verifica la firma del webhook
            Event event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);


            if ("checkout.session.completed".equals(event.getType())) {
                EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
                //deserializar el payload y extraer el orden_id de la metadata
                JsonObject payloadObject = JsonParser.parseString(payload).getAsJsonObject();
                JsonObject dataObject = payloadObject.getAsJsonObject().get("data").getAsJsonObject().get("object").getAsJsonObject();
                String orderId = dataObject.get("metadata").getAsJsonObject().get("orden_id").getAsString();
                String paymentIntentId = dataObject.get("payment_intent").getAsString();
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

                // Obtener el primer Charge asociado
                String chargeId = paymentIntent.getCharges().getData().get(0).getId();
                Charge charge = Charge.retrieve(chargeId);

                String paymentMethodId = charge.getPaymentMethod();

                System.out.println("Payment status: " + charge.getStatus());
                System.out.println("Customer email: " + charge.getBillingDetails().getEmail());
                System.out.println("Customer name: " + charge.getBillingDetails().getName());
                System.out.println("Card brand: " + charge.getPaymentMethodDetails().getCard().getBrand());
                System.out.println("Card brand: " + charge.getPaymentMethodDetails().getType());
                System.out.println("Fecha de Expiracion: " + charge.getPaymentMethodDetails().getCard().getExpMonth());
                System.out.println("Fecha de Expiracion (year): " + charge.getPaymentMethodDetails().getCard().getExpYear());
                System.out.println("Last 4 digits: " + charge.getPaymentMethodDetails().getCard().getLast4());
                System.out.println("Amount: " + charge.getAmount());
                System.out.println("Currency: " + charge.getCurrency());
                System.out.println("Orden ID: " + orderId);


                ComprobantePagoDTO comprobantePagoDTO = new ComprobantePagoDTO();
                comprobantePagoDTO.setIdOrden(Long.parseLong(orderId));
                comprobantePagoDTO.setMonto(BigDecimal.valueOf(charge.getAmount() / 100));
                comprobantePagoDTO.setNumero(charge.getId());

                ComprobantePago comprobantePago = comprobanteDePagoService.guardar(comprobantePagoDTO);

            }

            return event;
        } catch (Exception e) {
            // Maneja errores
            System.err.println("Error processing webhook: " + e.getMessage());
            throw new RuntimeException("Error processing webhook", e);
        }
    }
}
