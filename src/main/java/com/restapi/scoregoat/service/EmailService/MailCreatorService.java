package com.restapi.scoregoat.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailCreatorService {
    @Autowired
    private TemplateEngine templateEngine;

    public String buildResetPasswordEmail(String user, String newPassword) {
        Context context = new Context();
        context.setVariable("subject", "PASSWORD RESET:");
        context.setVariable("first_line", "User: ");
        context.setVariable("user", user);
        context.setVariable("second_line", "New Password: ");
        context.setVariable("newPassword", newPassword);
        context.setVariable("information_after", "Good Luck!");
        return templateEngine.process("mail/reset-password", context);
    }

    public String buildEmailVerificationEmail(String code) {
        Context context = new Context();
        context.setVariable("subject", "EMAIL VERIFICATION CODE:");
        context.setVariable("code", code);
        context.setVariable("information_after", "Thanks for joining :)");
        return templateEngine.process("mail/email-verification", context);
    }
}
