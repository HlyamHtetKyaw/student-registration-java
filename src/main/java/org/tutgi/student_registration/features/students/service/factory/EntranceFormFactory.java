package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;

@Component
public class EntranceFormFactory {

    public EntranceForm createFromRequest(EntranceFormRequest request, Student student) {
        EntranceForm form = new EntranceForm();
        form.setPermanentAddress(request.permanentAddress());
        form.setPermanentContactNumber(request.permanentPhoneNumber());
        form.assignStudent(student);
        return form;
    }
    
    public void updateFromPatch(EntranceForm form, EntranceFormUpdateRequest request) {
        Optional.ofNullable(request.permanentAddress()).ifPresent(form::setPermanentAddress);
        Optional.ofNullable(request.permanentPhoneNumber()).ifPresent(form::setPermanentContactNumber);
    }

}

