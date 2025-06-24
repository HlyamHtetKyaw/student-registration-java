package org.tutgi.student_registration.features.employee.admin.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tutgi.student_registration.data.enums.UserType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class StudentRegisterRequestValidationTest {

    @Autowired
    private Validator validator; 

    @Test
    void validStudentRegisterRequest_shouldPassValidation() {
        List<String> validNrcs = List.of(
        		"13/MASATA(N)",
                 		"၁၃/မဆတ(နိုင်)",
                 		"၁/ဒဖယ(နိုင်)",
                 		"၁၄/ငပတ(နိုင်)",
                "12/THAKATA(N)",
                "13/NAMKHAM(E)",
                 		"၄/မတန(ဧည့်)",
                "6/MATANA(T)"
        );
        validNrcs.stream().forEach(validNrc->{
        	StudentRegisterRequest req = new StudentRegisterRequest(UserType.STUDENT, "1001", validNrc);
            Set<ConstraintViolation<StudentRegisterRequest>> violations = validator.validate(req);
            assertTrue(violations.isEmpty(), "Expected no violations for: " + validNrc);
        });
    }

    @Test
    void invalidStudentRegisterRequest_shouldFailValidation() {
    	List<String> invalidNrcs = List.of(
    			"abc/မဆတ(နိုင်)",
    				"၁၃/မဆတနိုင်",
    		    "13MST(N)",
    		    		"၆/ကမ(နိုင်)",
    		    		"၁၃မဆတ(နိုင်)",
    		    		"၁၃/မဆတနိုင်",
    		    "",
    		    		"၁၃",
    		    		"၁၄/မတန(ဧည့်)", 
    		    		"၁၄/hh(ဧည့်)"
    		);

    	invalidNrcs.stream().forEach(invalidNrc->{
    		 StudentRegisterRequest req = new StudentRegisterRequest(UserType.STUDENT, "1001", invalidNrc);
    	        Set<ConstraintViolation<StudentRegisterRequest>> violations = validator.validate(req);
    	        assertFalse(violations.isEmpty(), "Expected violations for NRC: " + invalidNrc);
    	        boolean nrcViolation = violations.stream()
    	                .anyMatch(v -> v.getPropertyPath().toString().equals("nrc"));
    	        assertTrue(nrcViolation, "Expected violation on field 'nrc' for: " + invalidNrc);
    	});
    }

    @Test
    void emptyRollNo_shouldFailValidation() {
        StudentRegisterRequest req = new StudentRegisterRequest(UserType.STUDENT, "", "13/MASATA(N)");
        Set<ConstraintViolation<StudentRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rollNo")));
    }
    
    @Test
    void nullRollNo_shouldFailValidation() {
        StudentRegisterRequest req = new StudentRegisterRequest(UserType.STUDENT, null, "13/MASATA(N)");
        Set<ConstraintViolation<StudentRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("rollNo")));
    }
    
    @Test
    void nullNrc_shouldFailValidation() {
        StudentRegisterRequest req = new StudentRegisterRequest(UserType.STUDENT, "1001", null);
        Set<ConstraintViolation<StudentRegisterRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nrc")));
    }
}


