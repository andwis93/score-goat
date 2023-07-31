package com.restapi.scoregoat.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailCreatorService {
    @Autowired
    private TemplateEngine templateEngine;

    public String buildResetPasswordEmail(String user, String newPassword) {
        Context context = new Context();
        context.setVariable("preview_message", "Password reset was requested.");
        context.setVariable("first_line", "User: ");
        context.setVariable("user", user);
        context.setVariable("second_line", "New Password: ");
        context.setVariable("newPassword", newPassword);
        context.setVariable("information_after", "Good Luck!");
        return templateEngine.process("mail/reset-password", context);
    }
}
