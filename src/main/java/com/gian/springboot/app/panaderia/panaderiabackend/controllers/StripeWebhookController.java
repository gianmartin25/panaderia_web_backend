package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private static final String STRIPE_WEBHOOK_SECRET = "whsec_Y5vUMxAIcsL43A5u2zvkTWyX1IAqIeRh";

    @PostMapping("/stripe")
    public Map<String, String> handleStripeWebhook(HttpServletRequest request) {
        String payload;
        String sigHeader = request.getHeader("Stripe-Signature");
        Event event;

        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            payload = sb.toString();
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", "Error reading request payload");
            return response;
        }

        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", "Signature verification failed");
            return response;
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                // Handle the checkout session completion
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }
}
