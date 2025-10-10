package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.education.SubjectChoice;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;

@Component
public class SubjectChoiceFormFactory {

    public SubjectChoice createFromRequest(SubjectChoiceFormRequest request, Student student) {
        SubjectChoice form = new SubjectChoice();
//        form.setForm(request.);
//        form.setPermanentContactNumber(request.permanentPhoneNumber());
//        form.assignStudent(student);
        return form;
    }
    
    public void updateFromPatch(EntranceForm form, EntranceFormUpdateRequest request) {
        Optional.ofNullable(request.permanentAddress()).ifPresent(form::setPermanentAddress);
        Optional.ofNullable(request.permanentPhoneNumber()).ifPresent(form::setPermanentContactNumber);
    }

}
