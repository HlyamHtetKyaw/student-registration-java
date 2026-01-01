package org.tutgi.student_registration.config.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.event.EmailEvent;
import org.tutgi.student_registration.config.event.ModelEmailEvent;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailEventListener {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    @EventListener
    public void handleEmailEvent(final EmailEvent event) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(event.getToEmail());
            helper.setSubject(event.getSubject());
            helper.setText(event.getBody(), true);
            helper.addInline("logoImage", new ClassPathResource("templates/logo/tu_tgi.png"));

            mailSender.send(mimeMessage);
            log.info("MIME Email sent successfully to {}", event.getToEmail());
        } catch (Exception e) {
            log.error("Failed to send MIME email to {}", event.getToEmail(), e);
        }
    }
    
    @Async
    @EventListener
    public void sendModelMessage(final ModelEmailEvent event) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(event.getTo());
            helper.setSubject(event.getSubject());
            helper.setText(event.getBody(), true);

            helper.addInline("logoImage", new ClassPathResource("templates/logo/tu_tgi.png"));

            if (event.getAttachments() != null) {
                for (Resource attachment : event.getAttachments()) {
                    if (attachment != null && attachment.exists()) {
                        String originalFilename = attachment.getFilename();
                        String simpleName;

                        if (originalFilename != null && originalFilename.toLowerCase().contains("entrance")) {
                            simpleName = "Entrance Form.docx";
                        } else if (originalFilename != null && originalFilename.toLowerCase().contains("subject")) {
                            simpleName = "Subject Choice Form.docx";
                        } else if (originalFilename != null && originalFilename.toLowerCase().contains("registration")) {
                            simpleName = "Registration Form.docx";
                        } else {
                            simpleName = originalFilename;
                        }

                        helper.addAttachment(simpleName, attachment);
                    }
                }
            }
            mailSender.send(mimeMessage);
            log.info("MIME Email sent successfully to {}", event.getTo());

        } catch (Exception e) {
            log.error("Failed to send MIME email to {}", event.getTo(), e);
        }
    }

    
}
