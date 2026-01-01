package org.tutgi.student_registration.data.email.sender;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.tutgi.student_registration.data.email.AbstractEmailSender;

import jakarta.mail.MessagingException;

@Component
public class FormTemplateEmailSender extends AbstractEmailSender {

    @Override
    public void sendObjectModel(String to, Map<String, Object> model)
            throws MessagingException, IOException {

        Context context = new Context();
        context.setVariables(model);
        @SuppressWarnings("unchecked")
        List<Resource> attachments = (List<Resource>) model.get("attachments");
        String userName = extractUsername(to);
        String templateName = (String) model.get("templateName");
        String htmlTemplate = loadTemplate(templateName);
        String htmlContent = htmlTemplate
                .replace("{{username}}", userName);
        emailService.sendModelMessage(to, "Your Student Forms Are Submitted", htmlContent, attachments);
    }

    @Override
    public void send(String to, Map<String, String> params)
            throws MessagingException, IOException {
        throw new UnsupportedOperationException("Use sendObjectModel() instead for this sender");
    }
}


