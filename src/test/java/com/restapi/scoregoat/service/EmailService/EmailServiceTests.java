package com.restapi.scoregoat.service.EmailService;

import com.restapi.scoregoat.domain.EmailTypes;
import com.restapi.scoregoat.domain.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void shouldSendEmail() throws Exception {
        //Given
        Mail mail = new Mail("test@test.com", "Test", "User", "Password", null);

        //When
        emailService.send(mail, EmailTypes.RESET);

        //Then
        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }
}
