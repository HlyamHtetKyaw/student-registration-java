package org.tutgi.student_registration.features.students.service.utility;

import java.util.Set;

import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.personal.Parent;

public class ParentResolver {
    public static Parent resolve(Set<Parent> parents, ParentName type) {
        return parents.stream()
            .filter(p -> p.getParentType().getName() == type)
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException(type + " not found"));
    }
}

