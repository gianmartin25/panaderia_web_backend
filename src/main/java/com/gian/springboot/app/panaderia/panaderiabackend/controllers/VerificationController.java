package com.gian.springboot.app.panaderia.panaderiabackend.controllers;

import com.gian.springboot.app.panaderia.panaderiabackend.models.VerificationToken;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.VerificationTokenRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.repositories.UsuarioRepository;
import com.gian.springboot.app.panaderia.panaderiabackend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class VerificationController {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify")
    @ResponseBody
    public String verifyAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken == null) {
            return """
                        <html>
                        <body style="font-family: Arial, sans-serif; height: 100vh; text-align: center; margin-top: 50px; background: linear-gradient(to bottom, #f8f9fa, #e9ecef);">
                            <img src="/api/images/logo_black.jpg" alt="Logo Black" style="margin-top: 150px; width:300px; height:150px">
                            <h1 style="color: red;">Token inválido o expirado</h1>
                            <p>Por favor, verifica el enlace o solicita un nuevo correo de verificación.</p>
                        </body>
                        </html>
                    """;
        }

        // Mark the user as verified
        var usuario = verificationToken.getUsuario();
        usuario.setEliminado(false);
        usuario.setVerificado(true);
        usuarioRepository.save(usuario);

        // Send verification email
        String subject = "🎉 ¡Cuenta Verificada Exitosamente! 🎉";
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
                """.formatted(usuario.getUsername());

        try {
            emailService.sendAccountVerifiedEmail(
                    usuario.getCliente().get(0).getEmail(),
                    "🎉 ¡Cuenta Verificada Exitosamente! 🎉",
                    usuario.getUsername()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Generate HTML response
        return """
                    <html>
                    <body style="font-family: Arial, sans-serif; height: 100vh; text-align: center; margin-top: 50px; background: linear-gradient(to bottom, #f8f9fa, #e9ecef);">
                        <img src="/api/images/logo_black.jpg" alt="Logo Black" style="width:300px; height:150px">
                        <h1 style="color: #4CAF50;">¡Cuenta verificada exitosamente!</h1>
                        <p>Hola <strong>%s</strong>, tu cuenta ha sido verificada.</p>
                        <p>Detalles de tu cuenta:</p>
                        <ul style="list-style-type: none; padding: 0;">
                            <li><strong>Usuario:</strong> %s</li>
                            <li><strong>Email:</strong> %s</li>
                        </ul>
                        <p style="margin-top: 20px;">Gracias por registrarte en Panadería Cremas & Hojas.</p>
                        <a href="http://localhost:4200/auth/login" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Ir a Inicio de Sesión</a>
                    </body>
                    </html>
                """.formatted(usuario.getUsername(), usuario.getUsername(), usuario.getCliente().get(0).getEmail());
    }
}