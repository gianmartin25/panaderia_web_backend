package com.gian.springboot.app.panaderia.panaderiabackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendVerificationEmail_ShouldSendEmail() throws MessagingException {
        // Arrange
        String to = "test@example.com";
        String subject = "Verify your account";
        String verificationLink = "http://example.com/verify";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendVerificationEmail(to, subject, verificationLink);

        // Assert
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(captor.capture());
        MimeMessage sentMessage = captor.getValue();
        assertNotNull(sentMessage);
    }

    @Test
    void sendAccountVerifiedEmail_ShouldSendEmail() throws MessagingException {
        // Arrange
        String to = "test@example.com";
        String subject = "Account Verified";
        String username = "testuser";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendAccountVerifiedEmail(to, subject, username);

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailWithAttachment_ShouldSendEmailWithAttachment() throws MessagingException {
        String to = "test@example.com";
        String subject = "Invoice";
        String content = "Please find the attached invoice.";
        byte[] attachment = "Test PDF content".getBytes();
        String fileName = "invoice.pdf";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendEmailWithAttachment(to, subject, content, attachment, fileName);

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}