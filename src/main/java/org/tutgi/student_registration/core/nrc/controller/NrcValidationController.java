package org.tutgi.student_registration.core.nrc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.core.nrc.service.NrcValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class NrcValidationController {
	private final NrcValidationService nrcValidationService;

	@PostMapping("/nrc")
	public ResponseEntity<String> validateNrcData(@RequestBody final NrcRequest nrcRequest) {
		

		boolean isValid = nrcValidationService.validateNrc(nrcRequest.getData());

		if (isValid) {
			return ResponseEntity.ok("Validation successful for " + nrcRequest.getData());
		} else {
			return ResponseEntity.badRequest().body("Validation failed for " + nrcRequest.getData());
		}
	}
}
