package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.personal.Contact;

@Component
public class ContactFactory {

    public Contact createContact(String contactNumber, Long ownerId,EntityType entityType) {
    	Contact contact = new Contact();
    	contact.setContactNumber(contactNumber);
    	contact.setEntityId(ownerId);
        contact.setEntityType(entityType);
        return contact;
    }
    
    public void updateContact(Contact contact, String phoneNumber) {
        Optional.ofNullable(phoneNumber).ifPresent(contact::setContactNumber);
    }
}