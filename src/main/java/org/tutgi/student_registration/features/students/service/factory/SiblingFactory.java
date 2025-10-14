package org.tutgi.student_registration.features.students.service.factory;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.personal.Parent;
import org.tutgi.student_registration.data.models.personal.Sibling;
import org.tutgi.student_registration.data.repositories.ParentTypeRepository;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.ParentInfoProvider;
import org.tutgi.student_registration.features.students.dto.request.RegistrationFormRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SiblingFactory {

    private final ParentTypeRepository parentTypeRepository;

    public List<Sibling> createSibling(RegistrationFormRequest request,Student student) {
    	List<Sibling> siblings = request.siblings().stream().map(s->{
    		Sibling sibling = new Sibling();
    		sibling.setName(s.name());
        	sibling.setNrc(s.nrc());
        	return sibling;
    	}).toList();

        return siblings;
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
    
    public void updateParentFromSubjectChoice(Parent parent, ParentName type, ParentInfoProvider request) {
        if (type == ParentName.FATHER) {
            Optional.ofNullable(request.fatherNickname()).ifPresent(parent::setNickname);
            Optional.ofNullable(request.fatherEthnicity()).ifPresent(parent::setEthnicity);
            Optional.ofNullable(request.fatherReligion()).ifPresent(parent::setReligion);
            Optional.ofNullable(request.fatherDob()).ifPresent(parent::setDob);
            Optional.ofNullable(request.fatherPob()).ifPresent(parent::setPob);
        } else {
        	Optional.ofNullable(request.motherNickname()).ifPresent(parent::setNickname);
            Optional.ofNullable(request.motherEthnicity()).ifPresent(parent::setEthnicity);
            Optional.ofNullable(request.motherReligion()).ifPresent(parent::setReligion);
            Optional.ofNullable(request.motherDob()).ifPresent(parent::setDob);
            Optional.ofNullable(request.motherPob()).ifPresent(parent::setPob);
        }
    }
    
    public void updateParentFromRegistrationForm(Parent parent, ParentName type, RegistrationFormRequest request) {
        if (type == ParentName.FATHER) {
            Optional.ofNullable(request.fatherDeathDate()).ifPresent(parent::setDeathDate);
        } else {
        	Optional.ofNullable(request.motherDeathDate()).ifPresent(parent::setDeathDate);
        }
    }
}

