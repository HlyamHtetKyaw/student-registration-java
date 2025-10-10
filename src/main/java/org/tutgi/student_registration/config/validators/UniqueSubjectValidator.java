package org.tutgi.student_registration.config.validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tutgi.student_registration.config.annotations.UniqueSubjects;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest.SubjectScore;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueSubjectValidator implements ConstraintValidator<UniqueSubjects, SubjectChoiceFormRequest> {

    @Override
    public boolean isValid(SubjectChoiceFormRequest request, ConstraintValidatorContext context) {
        List<SubjectScore> scores = request.scores();
        Set<Long> subjectIds = new HashSet<>();
        for (SubjectScore score : scores) {
            if (!subjectIds.add(score.subjectId())) {
                return false;
            }
        }
        return true;
    }
}

