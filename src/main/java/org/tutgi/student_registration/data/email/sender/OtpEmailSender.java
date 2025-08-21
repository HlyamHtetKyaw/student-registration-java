package org.tutgi.student_registration.data.email.sender;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.email.AbstractEmailSender;

import jakarta.mail.MessagingException;

@Component
public class OtpEmailSender extends AbstractEmailSender {

    @Override
    public void send(String to, Map<String, String> params) throws MessagingException, IOException {
        String userName = extractUsername(to);
        String templateName = params.get("templateName");
        String resetCode = params.get("resetCode");
        String expTime = params.get("expTime");

        String htmlTemplate = loadTemplate(templateName);
        String htmlContent = htmlTemplate
                .replace("{{username}}", userName)
                .replace("{{code}}", resetCode)
                .replace("{{expTime}}", expTime);

        emailService.sendEmail(to, "Please Verify Your Email", htmlContent);
    }
}

