package com.restapi.scoregoat.service.EmailService;

import com.restapi.scoregoat.domain.EmailTypes;
import com.restapi.scoregoat.domain.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
   private final JavaMailSender javaMailSender;
    @Autowired
    private MailCreatorService mailCreatorService;

    public void send(final Mail mail, EmailTypes type) {
        log.info("Starting email preparation...");
        try {
            switch (type) {
                case RESET -> javaMailSender.send(createPasswordResetMessage(mail));
                case VERIFY -> javaMailSender.send(createEmailVerificationMessage(mail));
            }
            log.info("Email has been sent.");
        } catch (MailException e) {
            log.error("Failed to process email sending: " + e.getMessage(), e);
        }
    }

    private MimeMessagePreparator createPasswordResetMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mailCreatorService.buildResetPasswordEmail(mail.getUser(), mail.getCode()), true);
        };
    }

    private MimeMessagePreparator createEmailVerificationMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mailCreatorService.buildEmailVerificationEmail(mail.getCode()), true);
        };
    }
}
