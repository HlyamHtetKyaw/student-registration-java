package org.tutgi.student_registration.data.email.sender;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.email.AbstractEmailSender;

import jakarta.mail.MessagingException;

@Component
public class RejectionMailSender extends AbstractEmailSender {
	
    @Override
    public void send(String to, Map<String, String> params) throws MessagingException, IOException {
        String userName = extractUsername(to);
        String templateName = params.get("templateName");
        String rejectionData = params.get("rejectionData");

        String htmlTemplate = loadTemplate(templateName);
        String htmlContent = htmlTemplate
                .replace("{{username}}", userName)
                .replace("{{email}}", to)
                .replace("{{rejectionData}}", rejectionData);

        emailService.sendEmail(to, "Your Form Is Rejected", htmlContent);
    }

	@Override
	public void sendObjectModel(String to, Map<String, Object> model) throws MessagingException, IOException {
		throw new UnsupportedOperationException("Use send() instead for this sender");
	}
}

