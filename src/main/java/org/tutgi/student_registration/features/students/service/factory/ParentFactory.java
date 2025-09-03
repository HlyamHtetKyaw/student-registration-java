package org.tutgi.student_registration.features.students.service.factory;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.lookup.ParentType;
import org.tutgi.student_registration.data.models.personal.Parent;
import org.tutgi.student_registration.data.repositories.ParentTypeRepository;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.OptionalNrc;

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
    	if(type==ParentName.FATHER) {
    		request.fatherNameEng().ifPresent(parent::setEngName);
    		request.fatherNameMm().ifPresent(parent::setMmName);
    		request.fatherNrc().map(OptionalNrc::getValue).ifPresent(parent::setNrc);
    	}else {
    		request.motherNameEng().ifPresent(parent::setEngName);
    		request.motherNameMm().ifPresent(parent::setMmName);
    		request.motherNrc().map(OptionalNrc::getValue).ifPresent(parent::setNrc);
    	}
    }

}

