package org.tutgi.student_registration.data.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.tutgi.student_registration.config.service.EmailService;

import jakarta.mail.MessagingException;

public abstract class AbstractEmailSender {

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected SpringTemplateEngine templateEngine;

    protected String loadTemplate(String templateName) throws IOException {
        String path = "templates/mailTemplates/" + templateName + ".html";
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    protected String extractUsername(String email) {
        return email.split("@")[0];
    }

    public abstract void send(String to, Map<String, String> params)
            throws MessagingException, IOException;

    public abstract void sendObjectModel(String to, Map<String, Object> model)
            throws MessagingException, IOException;
}




