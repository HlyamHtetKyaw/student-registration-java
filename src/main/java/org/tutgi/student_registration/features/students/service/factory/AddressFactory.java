package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.personal.Address;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressFactory {
	
    public Address createAddress(String addressName, Long ownerId,EntityType entityType) {
    	Address address = new Address();
    	address.setAddress(addressName);
        address.setEntityId(ownerId);
        address.setEntityType(entityType);
        return address;
    }
    
    public void updateAddress(Address address, EntityType type, EntranceFormUpdateRequest request) {
    	Optional.ofNullable(request.address()).ifPresent(address::setAddress);
    }
}