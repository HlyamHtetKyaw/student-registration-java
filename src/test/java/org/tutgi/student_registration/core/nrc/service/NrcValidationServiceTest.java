package org.tutgi.student_registration.core.nrc.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class NrcValidationServiceTest {
	
	@Autowired
    private NrcValidationService nrcValidationService;
	@Test
	void testValidCases() {
		assertTrue(nrcValidationService.validateNrc("13/MASATA(N)"));
		assertTrue(nrcValidationService.validateNrc("၁၃/မဆတ(နိုင်)"));
		assertTrue(nrcValidationService.validateNrc("၁/ဒဖယ(နိုင်)"));
        assertTrue(nrcValidationService.validateNrc("၁၄/ငပတ(နိုင်)"));
        assertTrue(nrcValidationService.validateNrc("12/THAKATA(N)"));
        assertTrue(nrcValidationService.validateNrc("13/NAMKHAM(E)"));
        assertTrue(nrcValidationService.validateNrc("၄/မတန(ဧည့်)"));
        assertTrue(nrcValidationService.validateNrc("6/MATANA(T)"));
        
	}
	@Test
	void testInvalidCases() {
		assertFalse(nrcValidationService.validateNrc("abc/မဆတ(နိုင်)"));
		assertFalse(nrcValidationService.validateNrc("၁၃/မဆတနိုင်"));
		assertFalse(nrcValidationService.validateNrc(null));
		assertFalse(nrcValidationService.validateNrc("13MST(N)"));
		assertFalse(nrcValidationService.validateNrc("၆/ကမ(နိုင်)"));
		assertFalse(nrcValidationService.validateNrc("၁၃မဆတ(နိုင်)"));
		assertFalse(nrcValidationService.validateNrc("၁၃/မဆတနိုင်"));
		assertFalse(nrcValidationService.validateNrc(""));
		assertFalse(nrcValidationService.validateNrc("၁၃"));
		assertFalse(nrcValidationService.validateNrc("၁၄/မတန(ဧည့်)"));
		assertFalse(nrcValidationService.validateNrc("၁၄/hh(ဧည့်)"));
	}

}
