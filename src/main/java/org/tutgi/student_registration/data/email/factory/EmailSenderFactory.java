package org.tutgi.student_registration.data.email.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.data.email.AbstractEmailSender;

@Service
public class EmailSenderFactory {

    private final Map<String, AbstractEmailSender> senderMap;

    @Autowired
    public EmailSenderFactory(List<AbstractEmailSender> senders) {
        senderMap = new HashMap<>();
        for (AbstractEmailSender sender : senders) {
            senderMap.put(sender.getClass().getSimpleName(), sender);
        }
    }

    public AbstractEmailSender getSender(String senderName) {
        return senderMap.get(senderName);
    }
}

