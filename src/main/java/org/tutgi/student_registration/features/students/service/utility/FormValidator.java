package org.tutgi.student_registration.features.students.service.utility;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.data.models.form.Form;
import org.tutgi.student_registration.data.repositories.FormRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FormValidator {
	private final FormRepository formRepository;
	public Form valideForm(Long formId) {
		Form form = formRepository.findById(formId)
				.orElseThrow(() -> new EntityNotFoundException("Form not found with "+formId));
		if(!form.isOpen())throw new BadRequestException("Currently there is no active form.");
		return form;
	}
}
