package org.tutgi.student_registration.features.students.service.factory;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.education.MatriculationExamDetail;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;

@Component
public class MEDFactory {
	public MatriculationExamDetail createFromRequest(EntranceFormRequest request, Student student) {
		MatriculationExamDetail med = new MatriculationExamDetail();
        med.setYear(request.matriculationPassedYear());
        med.setDepartment(request.department());
        med.assignStudent(student);
        return med;
    }
    
    public void updateFromPatch(MatriculationExamDetail med, EntranceFormUpdateRequest request) {
    	request.matriculationPassedYear().ifPresent(med::setYear);
    	request.department().ifPresent(med::setDepartment);
    }
}
