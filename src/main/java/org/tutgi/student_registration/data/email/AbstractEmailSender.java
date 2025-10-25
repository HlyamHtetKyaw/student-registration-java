package org.tutgi.student_registration.data.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.tutgi.student_registration.config.service.EmailService;

import jakarta.mail.MessagingException;

public abstract class AbstractEmailSender {

    @Autowired
    protected EmailService emailService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Autowired
    protected SpringTemplateEngine templateEngine;

    protected String loadTemplate(String templateName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/mailTemplates/" + templateName + ".html");

        if (!resource.exists()) {
            throw new IOException("Email template not found in JAR: " + templateName);
        }

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




