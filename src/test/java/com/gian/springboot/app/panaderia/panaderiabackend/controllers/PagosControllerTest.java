//package com.gian.springboot.app.panaderia.panaderiabackend.controllers;
//
//import com.gian.springboot.app.panaderia.panaderiabackend.dtos.*;
//import com.gian.springboot.app.panaderia.panaderiabackend.models.ComprobantePago;
//import com.gian.springboot.app.panaderia.panaderiabackend.services.ComprobanteDePagoService;
//import com.gian.springboot.app.panaderia.panaderiabackend.services.GestorPagosService;
//import com.gian.springboot.app.panaderia.panaderiabackend.services.ProductoCheckoutService;
//import com.stripe.exception.SignatureVerificationException;
//import com.stripe.model.Event;
//import com.stripe.model.PaymentIntent;
//import com.stripe.model.PaymentIntentCollection;
//import com.stripe.model.checkout.Session;
//import com.stripe.net.Webhook;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PagosControllerTest {
//
//    @Mock
//    ProductoCheckoutService productoCheckoutService;
//
//    @Mock
//    GestorPagosService gestorPagosService;
//
//    @Mock
//    ComprobanteDePagoService comprobanteDePagoService;
//
//    @InjectMocks
//    PagosController pagosController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createCheckoutSession_createsSessionSuccessfully() {
//        PagosSesionDTO pagosSesionDTO = new PagosSesionDTO();
//        pagosSesionDTO.setProductos(new ProductoCartRequestDTO[]{new ProductoCartRequestDTO()});
//        pagosSesionDTO.setOrdenId(1L);
//
//        List<ProductoCartResponseDTO> productoCartResponseDTOS = new ArrayList<>();
//        ProductoCartResponseDTO producto = new ProductoCartResponseDTO();
//        producto.setIdProducto(1L);
//        producto.setDescripcion("Test Product");
//        producto.setNombre("Test Product");
//        producto.setPrecio(BigDecimal.valueOf(1000));
//        producto.setCantidad(1);
//        producto.setImagenUrl("test.jpg");
//        productoCartResponseDTOS.add(producto);
//
//        when(productoCheckoutService.verificarDisponibilidadProductos(anyList())).thenReturn(productoCartResponseDTOS);
//
//        Map<String, String> response = pagosController.createCheckoutSession(pagosSesionDTO);
//
//        assertNotNull(response);
//        assertTrue(response.containsKey("url"));
//    }
//
//    @Test
//    void createCheckoutSession_throwsExceptionWhenErrorOccurs() {
//        PagosSesionDTO pagosSesionDTO = new PagosSesionDTO();
//        pagosSesionDTO.setProductos(new ProductoCartRequestDTO[]{new ProductoCartRequestDTO()});
//        pagosSesionDTO.setOrdenId(1L);
//
//        when(productoCheckoutService.verificarDisponibilidadProductos(anyList())).thenThrow(new RuntimeException("Error"));
//
//        assertThrows(RuntimeException.class, () -> pagosController.createCheckoutSession(pagosSesionDTO));
//    }
//
//    @Test
//    void handleStripeWebhook_processesCompletedSessionSuccessfully() throws Exception {
//        String payload = "{ \"data\": { \"object\": { \"metadata\": { \"orden_id\": \"1\" }, \"payment_intent\": \"pi_123\" } } }";
//        String sigHeader = "test_signature";
//
//        Event event = mock(Event.class);
//        when(event.getType()).thenReturn("checkout.session.completed");
//        when(Webhook.constructEvent(payload, sigHeader, "whsec_VdZuMxYlTokIJV74YsXVzA6d1kMQZGZr")).thenReturn(event);
//
//        PaymentIntent paymentIntent = mock(PaymentIntent.class);
//        when(paymentIntent.getCharges()).thenReturn(mock(PaymentIntentCollection.class));
//        when(PaymentIntent.retrieve("pi_123")).thenReturn(paymentIntent);
//
//        pagosController.handleStripeWebhook(payload, sigHeader);
//
//        verify(gestorPagosService, times(1)).savePagoAndPagoTarjeta(any(PagoDTO.class), any(ComprobantePago.class));
//    }
//
//    @Test
//    void handleStripeWebhook_throwsExceptionWhenSignatureVerificationFails() {
//        String payload = "{ \"data\": { \"object\": { \"metadata\": { \"orden_id\": \"1\" }, \"payment_intent\": \"pi_123\" } } }";
//        String sigHeader = "invalid_signature";
//
//        when(Webhook.constructEvent(payload, sigHeader, "whsec_VdZuMxYlTokIJV74YsXVzA6d1kMQZGZr")).thenThrow(SignatureVerificationException.class);
//
//        assertThrows(RuntimeException.class, () -> pagosController.handleStripeWebhook(payload, sigHeader));
//    }
//}