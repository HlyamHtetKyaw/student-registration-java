package org.tutgi.student_registration.features.students.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.DuplicateEntityException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.models.education.MatriculationExamDetail;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.models.personal.Address;
import org.tutgi.student_registration.data.models.personal.Contact;
import org.tutgi.student_registration.data.models.personal.Job;
import org.tutgi.student_registration.data.models.personal.Parent;
import org.tutgi.student_registration.data.repositories.AddressRepository;
import org.tutgi.student_registration.data.repositories.ContactRepository;
import org.tutgi.student_registration.data.repositories.EntranceFormRepository;
import org.tutgi.student_registration.data.repositories.JobRepository;
import org.tutgi.student_registration.data.repositories.MatriculationExamDetailRepository;
import org.tutgi.student_registration.data.repositories.ParentRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.OptionalDob;
import org.tutgi.student_registration.features.students.dto.request.OptionalNrc;
import org.tutgi.student_registration.features.students.service.StudentService;
import org.tutgi.student_registration.features.students.service.factory.AddressFactory;
import org.tutgi.student_registration.features.students.service.factory.ContactFactory;
import org.tutgi.student_registration.features.students.service.factory.EntranceFormFactory;
import org.tutgi.student_registration.features.students.service.factory.JobFactory;
import org.tutgi.student_registration.features.students.service.factory.MEDFactory;
import org.tutgi.student_registration.features.students.service.factory.ParentFactory;
import org.tutgi.student_registration.features.users.utils.UserUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService{
	private final UserUtil userUtil;
	
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final JobRepository jobRepository;
    private final EntranceFormRepository entranceFormRepository;
    private final MatriculationExamDetailRepository medRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    
    private final ParentFactory parentFactory;
    private final JobFactory jobFactory;
    private final EntranceFormFactory entranceFormFactory;
    private final MEDFactory medFactory;
    private final ContactFactory contactFactory;
    private final AddressFactory addressFactory;
    
	@Override
	@Transactional
    public ApiResponse createEntranceForm(EntranceFormRequest request) {
        Long userId = userUtil.getCurrentUserInternal().userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            student = new Student();
            student.updatePersonalInfo(
                    request.studentNameEng(),
                    request.studentNameMm(),
                    request.studentNrc(),
                    request.ethnicity(),
                    request.religion(),
                    request.dob()
                );
            student.setUser(user);
            studentRepository.save(student);
        } else if (student.getEntranceForm() != null) {
            throw new DuplicateEntityException("Entrance Form already exists.");
        }
        
        Address studentAddr = addressFactory.createAddress(request.address(), student.getId(), EntityType.STUDENT);
        addressRepository.save(studentAddr);
        
        Contact studentContact = contactFactory.createContact(request.phoneNumber(), student.getId(), EntityType.STUDENT);
        contactRepository.save(studentContact);
        
        Parent father = parentFactory.createParent(request, ParentName.FATHER, student);
        Parent mother = parentFactory.createParent(request, ParentName.MOTHER, student);

        parentRepository.save(father);
        parentRepository.save(mother);

        Job fatherJob = jobFactory.createJob(request.fatherJob(), father.getId());
        Job motherJob = jobFactory.createJob(request.motherJob(), mother.getId());

        jobRepository.save(fatherJob);
        jobRepository.save(motherJob);

        EntranceForm entranceForm = entranceFormFactory.createFromRequest(request, student);
        entranceFormRepository.save(entranceForm);
        
        MatriculationExamDetail medForm = medFactory.createFromRequest(request, student);
        medRepository.save(medForm);
        
        studentRepository.save(student);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Entrance Form registered successfully.")
                .data(true)
                .build();
    }
	
	@Override
	@Transactional
	public ApiResponse updateEntranceForm(EntranceFormUpdateRequest request) {
	    Long userId = userUtil.getCurrentUserInternal().userId();
	    Student student = studentRepository.findByUserId(userId);
	    
	    if (student == null || student.getEntranceForm() == null) {
	        throw new EntityNotFoundException("Entrance Form not found for user");
	    }
	    
	    request.studentNameEng().ifPresent(student::setEngName);
        request.studentNameMm().ifPresent(student::setMmName);
        request.studentNrc().map(OptionalNrc::getValue).ifPresent(student::setNrc);
        request.ethnicity().ifPresent(student::setEthnicity);
        request.religion().ifPresent(student::setReligion);
        request.dob().map(OptionalDob::getValue).ifPresent(student::setDob);
	    
	    EntranceForm form = student.getEntranceForm();
	    entranceFormFactory.updateFromPatch(form, request);
	    
	    MatriculationExamDetail medForm = student.getMatriculationExamDetail();
	    medFactory.updateFromPatch(medForm, request);
	    
	    Parent father = parentRepository.findByStudentIdAndParentType_Name(student.getId(), ParentName.FATHER)
	        .orElseThrow(() -> new EntityNotFoundException("Father entity not found"));
	    parentFactory.updateParent(father, ParentName.FATHER, request);
	    Parent mother = parentRepository.findByStudentIdAndParentType_Name(student.getId(), ParentName.MOTHER)
	        .orElseThrow(() -> new EntityNotFoundException("Mother entity not found"));
	    parentFactory.updateParent(mother, ParentName.MOTHER, request);
	    
	    Job fatherJob = jobRepository.findByEntityId(father.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Father's job not found"));
	    jobFactory.updateJob(fatherJob, ParentName.FATHER, request);
	    Job motherJob = jobRepository.findByEntityId(mother.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Mother's job not found"));
	    jobFactory.updateJob(fatherJob, ParentName.MOTHER, request);
	    
	    Address studentAddr = addressRepository.findByEntityId(student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's address not found"));
	    addressFactory.updateAddress(studentAddr, studentAddr.getEntityType(), request);
	    
	    Contact studentContact = contactRepository.findByEntityId(student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's contact number not found"));
	    contactFactory.updateContact(studentContact, studentContact.getEntityType(), request);
	    
	    studentRepository.save(student);
	    entranceFormRepository.save(form);
	    medRepository.save(medForm);
	    parentRepository.save(father);
	    parentRepository.save(mother);
	    jobRepository.save(fatherJob);
	    jobRepository.save(motherJob);
	    addressRepository.save(studentAddr);
	    contactRepository.save(studentContact);
	    
	    return ApiResponse.builder()
	            .success(1)
	            .code(HttpStatus.OK.value())
	            .message("Entrance Form updated successfully.")
	            .data(true)
	            .build();
	}

}
