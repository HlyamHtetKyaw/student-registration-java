package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.lookup.ParentType;
import org.tutgi.student_registration.data.models.personal.Parent;
import org.tutgi.student_registration.data.repositories.ParentTypeRepository;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParentFactory {

    private final ParentTypeRepository parentTypeRepository;

    public Parent createParent(EntranceFormRequest request, ParentName parentName, Student student) {
        Parent parent = new Parent();
        
        if (parentName == ParentName.FATHER) {
            parent.setEngName(request.fatherNameEng());
            parent.setMmName(request.fatherNameMm());
            parent.setNrc(request.fatherNrc());
        } else {
            parent.setEngName(request.motherNameEng());
            parent.setMmName(request.motherNameMm());
            parent.setNrc(request.motherNrc());
        }

        ParentType parentType = parentTypeRepository.findByName(parentName)
                .orElseThrow(() -> new EntityNotFoundException("Parent type not found: " + parentName));
        parent.setParentType(parentType);
        parent.assignStudent(student);

        return parent;
    }
    
    public void updateParent(Parent parent, ParentName type, EntranceFormUpdateRequest request) {
        if (type == ParentName.FATHER) {
            Optional.ofNullable(request.fatherNameEng()).ifPresent(parent::setEngName);
            Optional.ofNullable(request.fatherNameMm()).ifPresent(parent::setMmName);
            Optional.ofNullable(request.fatherNrc()).ifPresent(parent::setNrc);
        } else {
            Optional.ofNullable(request.motherNameEng()).ifPresent(parent::setEngName);
            Optional.ofNullable(request.motherNameMm()).ifPresent(parent::setMmName);
            Optional.ofNullable(request.motherNrc()).ifPresent(parent::setNrc);
        }
    }


}

