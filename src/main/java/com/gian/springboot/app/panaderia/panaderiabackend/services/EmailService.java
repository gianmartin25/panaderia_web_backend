package com.gian.springboot.app.panaderia.panaderiabackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String subject, String verificationLink) throws  MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        // HTML content with an embedded image
        String content = """
                <html>
                <body>
                    <h1 style="color: #4CAF50;">¡Bienvenido a Panadería Cremas & Hojas!</h1>
                    <img src="cid:logoImage" alt="Logo" style="width: 200px;"/>
                    <p>Gracias por registrarte. Por favor, verifica tu cuenta haciendo clic en el siguiente enlace:</p>
                    <a href="%s" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Verificar Cuenta</a>
                    <br><br>
                     <p style='margin-top: 20px; color: #888;'>Este enlace expirará en 24 horas.</p>
                </body>
                </html>
                """.formatted(verificationLink);

        helper.setText(content, true);

        // Attach an image
        helper.addInline("logoImage", new ClassPathResource("static/images/logo_black.jpg"));

        mailSender.send(message);
    }

    public void sendAccountVerifiedEmail(String to, String subject, String username) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        // HTML content for account verification success
        String content = """
            <html>
            <body style="font-family: Arial, sans-serif; text-align: center; background-color: #f9f9f9; padding: 20px;">
                <img src="cid:logoImage" alt="Logo" style="width: 100px; margin-top: 20px;">
                <h1 style="color: #4CAF50;">🎉 ¡Felicidades, %s! 🎉</h1>
                <p style="font-size: 18px; color: #333;">Tu cuenta ha sido verificada exitosamente. 🥳</p>
                <p style="font-size: 16px; color: #555;">Ahora puedes disfrutar de todos los beneficios de Panadería Cremas & Hojas. 🥐</p>
                <a href="http://localhost:4200/auth/login" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Ir a Inicio de Sesión</a>
                <p style="margin-top: 20px; font-size: 14px; color: #888;">Gracias por elegirnos. ¡Esperamos verte pronto! 🛒</p>
            </body>
            </html>
        """.formatted(username);

        helper.setText(content, true);

        // Attach the logo
        helper.addInline("logoImage", new ClassPathResource("static/images/logo_black.jpg"));

        mailSender.send(message);
    }


    public void sendEmailWithAttachment(String to, String subject, String content, byte[] attachment, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        // Attach the logo
        helper.addInline("logoImage", new ClassPathResource("static/images/logo_black.jpg"));

        // Add the PDF attachment
        helper.addAttachment(fileName, () -> new ByteArrayInputStream(attachment));

        mailSender.send(message);
    }
}