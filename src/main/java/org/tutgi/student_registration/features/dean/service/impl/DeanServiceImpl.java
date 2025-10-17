package org.tutgi.student_registration.features.dean.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginationMeta;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.features.dean.dto.response.SubmittedStudentResponse;
import org.tutgi.student_registration.features.dean.service.DeanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeanServiceImpl implements DeanService {
	
	private final StudentRepository studentRepository;
	
	@Override
	public PaginatedApiResponse<SubmittedStudentResponse> getAllSubmittedData(String keyword,Pageable pageable) {
	 Page<Student> studentPage = this.studentRepository.findAllFiltered(keyword, pageable);
	
	    List<SubmittedStudentResponse> studentResponses = studentPage.getContent().stream()
	            .map(student -> SubmittedStudentResponse.builder()
	                    .studentId(student.getId())
	                    .studentNameEng(student.getEngName())
	                    .studentNameMM(student.getMmName())
	                    .createdAt(student.getCreatedAt())
	                    .updatedAt(student.getUpdatedAt())
	                    .build()
	            ).toList();
	
	    PaginationMeta meta = new PaginationMeta();
	    meta.setTotalItems(studentPage.getTotalElements());
	    meta.setTotalPages(studentPage.getTotalPages());
	    meta.setCurrentPage(pageable.getPageNumber()+1);
	    return PaginatedApiResponse.<SubmittedStudentResponse>builder()
	            .success(1)
	            .code(HttpStatus.OK.value())
	            .message("Fetched successfully")
	            .meta(meta)
	            .data(studentResponses)
	            .build();
	}
}
