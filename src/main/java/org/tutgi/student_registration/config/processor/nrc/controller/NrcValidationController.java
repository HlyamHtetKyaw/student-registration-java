package org.tutgi.student_registration.config.processor.nrc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.config.processor.nrc.service.NrcValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class NrcValidationController {
	private final NrcValidationService nrcValidationService;

	@PostMapping("/nrc")
	public ResponseEntity<String> validateNrcData(@RequestBody final NrcRequest nrcRequest) {
		// Extract only the part before the trailing numbers
		String nrcPartToValidate = nrcRequest.getData();
		int lastParenIndex = nrcRequest.getData().lastIndexOf(")");
		if (lastParenIndex != -1 && lastParenIndex + 1 < nrcRequest.getData().length()
				&& Character.isDigit(nrcRequest.getData().charAt(lastParenIndex + 1))) {
			nrcPartToValidate = nrcRequest.getData().substring(0, lastParenIndex + 1);
		}

		boolean isValid = nrcValidationService.validateNrc(nrcPartToValidate);

		if (isValid) {
			return ResponseEntity.ok("Validation successful for " + nrcPartToValidate);
		} else {
			return ResponseEntity.badRequest().body("Validation failed for " + nrcPartToValidate);
		}
	}
}
