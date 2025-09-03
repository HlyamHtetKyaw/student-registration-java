package org.tutgi.student_registration.features.students.service.factory;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.OptionalPhoneNumber;

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
    	request.permanentAddress().ifPresent(form::setPermanentAddress);
    	request.permanentPhoneNumber().map(OptionalPhoneNumber::getValue).ifPresent(form::setPermanentContactNumber);
    }
}

