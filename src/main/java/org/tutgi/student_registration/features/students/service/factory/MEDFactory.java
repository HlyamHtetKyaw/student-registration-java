package org.tutgi.student_registration.features.students.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.education.MatriculationExamDetail;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;

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
	    Optional.ofNullable(request.matriculationPassedYear()).ifPresent(med::setYear);
	    Optional.ofNullable(request.department()).ifPresent(med::setDepartment);
	}
	
	public void updateMedFromSubjectChoice(MatriculationExamDetail med, String rollNumber) {
		 Optional.ofNullable(rollNumber).ifPresent(med::setRollNumber);
	}
}
