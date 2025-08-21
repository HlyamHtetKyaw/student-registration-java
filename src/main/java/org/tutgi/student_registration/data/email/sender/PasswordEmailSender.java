package org.tutgi.student_registration.data.email.sender;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.email.AbstractEmailSender;

import jakarta.mail.MessagingException;

@Component
public class PasswordEmailSender extends AbstractEmailSender {
	
	@Value("${frontend.login.url}")
    protected String frontendUrl;
	
    @Override
    public void send(String to, Map<String, String> params) throws MessagingException, IOException {
        String userName = extractUsername(to);
        String templateName = params.get("templateName");
        String password = params.get("password");

        String htmlTemplate = loadTemplate(templateName);
        String htmlContent = htmlTemplate
                .replace("{{username}}", userName)
                .replace("{{email}}", to)
                .replace("{{password}}", password)
                .replace("{{frontendUrl}}", frontendUrl);

        emailService.sendEmail(to, "Your Account Is Created", htmlContent);
    }
}

