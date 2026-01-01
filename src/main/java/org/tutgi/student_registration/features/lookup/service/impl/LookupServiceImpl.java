package org.tutgi.student_registration.features.lookup.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.lookup.Major;
import org.tutgi.student_registration.data.models.lookup.Subject;
import org.tutgi.student_registration.data.repositories.MajorRepository;
import org.tutgi.student_registration.data.repositories.SubjectRepository;
import org.tutgi.student_registration.features.lookup.dto.response.MajorResponse;
import org.tutgi.student_registration.features.lookup.dto.response.SubjectResponse;
import org.tutgi.student_registration.features.lookup.service.LookupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LookupServiceImpl implements LookupService{

    private final SubjectRepository subjectRepository;
    private final MajorRepository majorRepository;
    
	@Override
	@Cacheable(value = "allSubjectsCache")
	public ApiResponse getAllSubjects() {
		List<Subject> subjects = subjectRepository.findAll();
		List<SubjectResponse> subjectsData = subjects.stream()
                .map(subject -> new SubjectResponse(subject.getId(),subject.getName().getDisplayName()))
                .collect(Collectors.toList());
		 return ApiResponse.builder()
	                .success(1)
	                .code(HttpStatus.OK.value())
	                .data(subjectsData)
	                .message("Subjects data are fetched successfully")
	                .build();
	}
	
	@Override
    @Cacheable(value = "allMajorsCache")
    public ApiResponse getAllMajors() {
        List<Major> majors = majorRepository.findAll();

        List<MajorResponse> majorResponse = majors.stream()
            .map(major -> new MajorResponse(
                major.getId(),
                major.getName().name(),
                major.getEngName(),
                major.getMmName()
            ))
            .collect(Collectors.toList());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(majorResponse)
                .message("Majors data are fetched successfully")
                .build();
    }
}

