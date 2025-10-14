package org.tutgi.student_registration.features.students.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.exceptions.DuplicateEntityException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.models.education.MatriculationExamDetail;
import org.tutgi.student_registration.data.models.education.SubjectChoice;
import org.tutgi.student_registration.data.models.education.SubjectExam;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.models.form.Form;
import org.tutgi.student_registration.data.models.form.MajorSubjectChoiceForm;
import org.tutgi.student_registration.data.models.lookup.Major;
import org.tutgi.student_registration.data.models.lookup.Subject;
import org.tutgi.student_registration.data.models.personal.Address;
import org.tutgi.student_registration.data.models.personal.Contact;
import org.tutgi.student_registration.data.models.personal.Job;
import org.tutgi.student_registration.data.models.personal.Parent;
import org.tutgi.student_registration.data.models.personal.Sibling;
import org.tutgi.student_registration.data.repositories.AddressRepository;
import org.tutgi.student_registration.data.repositories.ContactRepository;
import org.tutgi.student_registration.data.repositories.EntranceFormRepository;
import org.tutgi.student_registration.data.repositories.JobRepository;
import org.tutgi.student_registration.data.repositories.MajorRepository;
import org.tutgi.student_registration.data.repositories.MajorSubjectChoiceFormRepository;
import org.tutgi.student_registration.data.repositories.MatriculationExamDetailRepository;
import org.tutgi.student_registration.data.repositories.ParentRepository;
import org.tutgi.student_registration.data.repositories.SiblingRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.repositories.SubjectChoiceRepository;
import org.tutgi.student_registration.data.repositories.SubjectExamRepository;
import org.tutgi.student_registration.data.repositories.SubjectRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.form.dto.response.FormResponse;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.RegistrationFormRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest.MajorChoice;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest.SubjectScore;
import org.tutgi.student_registration.features.students.dto.request.UpdateSubjectChoiceFormRequest;
import org.tutgi.student_registration.features.students.dto.response.EntranceFormResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse.MajorChoiceResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse.SubjectScoreResponse;
import org.tutgi.student_registration.features.students.service.StudentService;
import org.tutgi.student_registration.features.students.service.factory.AddressFactory;
import org.tutgi.student_registration.features.students.service.factory.ContactFactory;
import org.tutgi.student_registration.features.students.service.factory.EntranceFormFactory;
import org.tutgi.student_registration.features.students.service.factory.JobFactory;
import org.tutgi.student_registration.features.students.service.factory.MEDFactory;
import org.tutgi.student_registration.features.students.service.factory.ParentFactory;
import org.tutgi.student_registration.features.students.service.factory.SubjectChoiceFormFactory;
import org.tutgi.student_registration.features.students.service.utility.FormValidator;
import org.tutgi.student_registration.features.students.service.utility.ParentResolver;
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
    private final SubjectChoiceRepository subjectChoiceRepository;
    private final MatriculationExamDetailRepository medRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    private final SubjectRepository subjectRepository;
    private final MajorRepository majorRepository;
    private final SubjectExamRepository subjectExamRepository;
    private final MajorSubjectChoiceFormRepository majorSubjectChoiceFormRepository;
    private final SiblingRepository siblingRepository;
    
    private final FormValidator formValidator;
    
    private final ParentFactory parentFactory;
    private final JobFactory jobFactory;
    private final EntranceFormFactory entranceFormFactory;
    private final SubjectChoiceFormFactory subjectChoiceFormFactory;
    private final MEDFactory medFactory;
    private final ContactFactory contactFactory;
    private final AddressFactory addressFactory;
    
    private final ModelMapper modelMapper;
	@Override
	@Transactional
    public ApiResponse createEntranceForm(EntranceFormRequest request) {
		Form form = formValidator.valideForm(request.formId());
		Long userId = userUtil.getCurrentUserInternal().userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            student = new Student();
            student.updatePersonalInfo(
            		request.enrollmentNumber(),
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
        entranceForm.assignForm(form);
        entranceFormRepository.save(entranceForm);
        
        MatriculationExamDetail medForm = medFactory.createFromRequest(request, student);
        medRepository.save(medForm);
        
        studentRepository.save(student);
        
        EntranceFormResponse response = buildEntranceFormResponse(
                form, student, medForm,
                father, fatherJob,
                mother, motherJob,
                studentAddr, studentContact,
                entranceForm, modelMapper);
        
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Entrance Form registered successfully.")
                .data(response)
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
	    
	    Optional.ofNullable(request.enrollmentNumber()).ifPresent(student::setEnrollmentNumber);
	    Optional.ofNullable(request.studentNameEng()).ifPresent(student::setEngName);
	    Optional.ofNullable(request.studentNameMm()).ifPresent(student::setMmName);
	    Optional.ofNullable(request.studentNrc()).ifPresent(student::setNrc);
	    Optional.ofNullable(request.ethnicity()).ifPresent(student::setEthnicity);
	    Optional.ofNullable(request.religion()).ifPresent(student::setReligion);
	    Optional.ofNullable(request.dob()).ifPresent(student::setDob);
	    
	    EntranceForm form = student.getEntranceForm();
	    entranceFormFactory.updateFromPatch(form,request);
	    
	    MatriculationExamDetail medForm = student.getMatriculationExamDetail();
	    medFactory.updateFromPatch(medForm,request);
	    
	    Parent father = parentRepository.findByStudentIdAndParentType_Name(student.getId(),ParentName.FATHER)
	        .orElseThrow(() -> new EntityNotFoundException("Father entity not found"));
	    parentFactory.updateParent(father, ParentName.FATHER,request);
	    Parent mother = parentRepository.findByStudentIdAndParentType_Name(student.getId(),ParentName.MOTHER)
	        .orElseThrow(() -> new EntityNotFoundException("Mother entity not found"));
	    parentFactory.updateParent(mother,ParentName.MOTHER,request);
	    
	    Job fatherJob = jobRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,father.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Father's job not found"));
	    jobFactory.updateJob(fatherJob,ParentName.FATHER,request);
	    Job motherJob = jobRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,mother.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Mother's job not found"));
	    jobFactory.updateJob(motherJob,ParentName.MOTHER,request);
	    
	    Address studentAddr = addressRepository.findByEntityTypeAndEntityId(EntityType.STUDENT,student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's address not found"));
	    addressFactory.updateAddress(studentAddr,request.address());
	    
	    Contact studentContact = contactRepository.findByEntityTypeAndEntityId(EntityType.STUDENT,student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's contact number not found"));
	    contactFactory.updateContact(studentContact,request.phoneNumber());
	    
//	    studentRepository.save(student);
//	    entranceFormRepository.save(form);
//	    medRepository.save(medForm);
//	    parentRepository.save(father);
//	    parentRepository.save(mother);
//	    jobRepository.save(fatherJob);
//	    jobRepository.save(motherJob);
//	    addressRepository.save(studentAddr);
//	    contactRepository.save(studentContact);
	    
	    return ApiResponse.builder()
	            .success(1)
	            .code(HttpStatus.OK.value())
	            .message("Entrance Form updated successfully.")
	            .data(true)
	            .build();
	}
	
	@Override
	public ApiResponse getEntranceForm() {
	    Long userId = userUtil.getCurrentUserInternal().userId();
	    Student student = studentRepository.findByUserId(userId);

	    if (student == null || student.getEntranceForm() == null) {
	        throw new EntityNotFoundException("Entrance form not found");
	    }

	    EntranceForm entranceForm = student.getEntranceForm();
	    MatriculationExamDetail medForm = student.getMatriculationExamDetail();
	    Parent father = ParentResolver.resolve(student.getParents(),ParentName.FATHER);
	    Parent mother = ParentResolver.resolve(student.getParents(),ParentName.MOTHER);
	    Job fatherJob = jobRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,father.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Father's job not found"));
	    Job motherJob = jobRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,mother.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Mother's job not found"));
	    Address studentAddr = addressRepository.findByEntityTypeAndEntityId(EntityType.STUDENT,student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's address not found"));
	    Contact studentContact = contactRepository.findByEntityTypeAndEntityId(EntityType.STUDENT,student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's contact number not found"));
	    
	    Form formData = entranceForm.getForm();
	    EntranceFormResponse response = buildEntranceFormResponse(
	            formData, student, medForm,
	            father, fatherJob,
	            mother, motherJob,
	            studentAddr, studentContact,
	            entranceForm, modelMapper);


	    return ApiResponse.builder()
	        .success(1)
	        .code(HttpStatus.OK.value())
	        .message("Entrance Form retrieved successfully.")
	        .data(response)
	        .build();
	}
	
	public EntranceFormResponse buildEntranceFormResponse(
	        Form formData,
	        Student student,
	        MatriculationExamDetail medForm,
	        Parent father,
	        Job fatherJob,
	        Parent mother,
	        Job motherJob,
	        Address studentAddr,
	        Contact studentContact,
	        EntranceForm entranceForm,
	        ModelMapper modelMapper) {

	    return EntranceFormResponse.builder()
	            .formData(modelMapper.map(formData, FormResponse.class))
	            .enrollmentNumber(student.getEnrollmentNumber())
	            .studentNameMm(student.getMmName())
	            .studentNameEng(student.getEngName())
	            .studentNrc(student.getNrc())
	            .ethnicity(student.getEthnicity())
	            .religion(student.getReligion())
	            .dob(student.getDob())
	            .matriculationPassedYear(medForm.getYear())
	            .department(medForm.getDepartment())
	            .fatherNameMm(father.getMmName())
	            .fatherNameEng(father.getEngName())
	            .fatherNrc(father.getNrc())
	            .fatherJob(fatherJob.getName())
	            .motherNameMm(mother.getMmName())
	            .motherNameEng(mother.getEngName())
	            .motherNrc(mother.getNrc())
	            .motherJob(motherJob.getName())
	            .address(studentAddr.getAddress())
	            .phoneNumber(studentContact.getContactNumber())
	            .permanentAddress(entranceForm.getPermanentAddress())
	            .permanentPhoneNumber(entranceForm.getPermanentContactNumber())
	            .build();
	}

	@Override
	@Transactional
	public ApiResponse createSubjectChoiceForm(SubjectChoiceFormRequest request) {
		Form form = formValidator.valideForm(request.formId());
		Long userId = userUtil.getCurrentUserInternal().userId();

        Student student = studentRepository.findByUserId(userId);
        if(student==null) {
        	throw new EntityNotFoundException("Student entity not found");
        }
        if(student.getSubjectChoice()!=null) {
        	throw new DuplicateEntityException("Form already exits");
        }
        List<SubjectScore> subjectScores = request.subjectScores();
        List<MajorChoice> majorChoice = request.majorChoices();
        
	     if (subjectScores.size() != 6 || majorChoice.size() != 6) {
	         throw new IllegalArgumentException("Exactly 6 subjects or majors must be provided.");
	     }
     
        Optional.ofNullable(request.studentNickname()).ifPresent(student::setNickname);
        Optional.ofNullable(request.studentPob()).ifPresent(student::setPob);
        
        Parent father = parentRepository.findByStudentIdAndParentType_Name(student.getId(),ParentName.FATHER)
    	        .orElseThrow(() -> new EntityNotFoundException("Father entity not found"));
        parentFactory.updateParentFromSubjectChoice(father, ParentName.FATHER,request);
        
        Parent mother = parentRepository.findByStudentIdAndParentType_Name(student.getId(),ParentName.MOTHER)
    	        .orElseThrow(() -> new EntityNotFoundException("Mother entity not found"));
        parentFactory.updateParentFromSubjectChoice(mother, ParentName.MOTHER,request);
        
        Contact fatherContact = contactFactory.createContact(request.fatherPhoneNumber(), father.getId(), EntityType.PARENTS);
        contactRepository.save(fatherContact);
        
        Contact motherContact = contactFactory.createContact(request.motherPhoneNumber(), mother.getId(), EntityType.PARENTS);
        contactRepository.save(motherContact);
        
        MatriculationExamDetail medForm = student.getMatriculationExamDetail();
        long totalScore = request.subjectScores().stream()
	    	    .mapToLong(score -> score.score())
	    	    .sum();
	    medForm.setTotalScore(totalScore);
	    medFactory.updateMedFromSubjectChoice(medForm,request.matriculationRollNumber());
	    
	    Address fatherAddress = addressFactory.createAddress(request.fatherAddress(), father.getId(), EntityType.PARENTS);
        addressRepository.save(fatherAddress);
        Address motherAddress = addressFactory.createAddress(request.motherAddress(), mother.getId(), EntityType.PARENTS);
        addressRepository.save(motherAddress);
        
        SubjectChoice subjectChoice = subjectChoiceFormFactory.createFromRequest(request, student);
        subjectChoice.assignForm(form);
        subjectChoiceRepository.save(subjectChoice);
        
        studentRepository.save(student);
        parentRepository.save(father);
	    parentRepository.save(mother);
	    medRepository.save(medForm);
	    
	    List<SubjectExam> subjectExams = request.subjectScores().stream()
	        .map(score -> {
	            Subject subject = subjectRepository.findByName(score.subjectName())
	                .orElseThrow(() -> new EntityNotFoundException("Subject not found: " + score.subjectName()));
	            return new SubjectExam(subject, medForm, score.score().longValue());
	        })
	        .toList();
	    subjectExamRepository.saveAll(subjectExams);

	    List<MajorSubjectChoiceForm> majorChoices = request.majorChoices().stream()
	        .map(mc -> {
	            Major major = majorRepository.findByName(mc.majorName())
	                .orElseThrow(() -> new EntityNotFoundException("Major not found: " + mc.majorName()));
	            return new MajorSubjectChoiceForm(major, subjectChoice, mc.priorityScore());
	        })
	        .toList();
	    majorSubjectChoiceFormRepository.saveAll(majorChoices);
	    
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Form registered successfully.")
                .data(true)
                .build();
	}

	@Override
	@Transactional
	public ApiResponse updateSubjectChoiceForm(UpdateSubjectChoiceFormRequest request) {
		Long userId = userUtil.getCurrentUserInternal().userId();
		Student student = studentRepository.findByUserId(userId);
		if(student.getSubjectChoice()==null)throw new BadRequestException("Form does not exit");
		
		Optional.ofNullable(request.studentNickname()).ifPresent(student::setNickname);
        Optional.ofNullable(request.studentPob()).ifPresent(student::setPob);
        
        updateParentAndContact(request, ParentName.FATHER, student);
        updateParentAndContact(request, ParentName.MOTHER, student);

        MatriculationExamDetail medForm = student.getMatriculationExamDetail();
	    medFactory.updateMedFromSubjectChoice(medForm,request.matriculationRollNumber());
	    AtomicLong previousScore = new AtomicLong(0);
	    AtomicLong newScore = new AtomicLong(0);

	    request.subjectScores().forEach(score -> {
	        SubjectExam subjectExam = subjectExamRepository
	            .findBySubject_NameAndMed_Id(score.subjectName(), medForm.getId())
	            .orElseThrow(() -> new EntityNotFoundException(
	                "SubjectExam not found for subject: " + score.subjectName() + " and MED ID: " + medForm.getId()));

	        previousScore.addAndGet(subjectExam.getScore());
	        newScore.addAndGet(score.score().longValue());

	        subjectExam.setScore(score.score().longValue());
	    });

	    medForm.setTotalScore((medForm.getTotalScore() - previousScore.get()) + newScore.get());
	    List<MajorChoice> majorChoice = request.majorChoices();
	    if(majorChoice!=null) {
	    	if (majorChoice.size() != 6) throw new IllegalArgumentException("Exactly 6 majors must be provided.");
	    	majorSubjectChoiceFormRepository.deleteBySubjectChoiceId(student.getSubjectChoice().getId());
		    List<MajorSubjectChoiceForm> majorChoices = request.majorChoices().stream()
			        .map(mc -> {
			            Major major = majorRepository.findByName(mc.majorName())
			                .orElseThrow(() -> new EntityNotFoundException("Major not found: " + mc.majorName()));
			            return new MajorSubjectChoiceForm(major, student.getSubjectChoice(), mc.priorityScore());
			        })
			        .toList();
			majorSubjectChoiceFormRepository.saveAll(majorChoices);
	    }
	    
	    return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Form updated successfully.")
                .data(true)
                .build();
	}
	
	private void updateParentAndContact(UpdateSubjectChoiceFormRequest request, ParentName parentName, Student student) {
	    Parent parent = parentRepository.findByStudentIdAndParentType_Name(student.getId(), parentName)
	        .orElseThrow(() -> new EntityNotFoundException(parentName + " entity not found"));
	    parentFactory.updateParentFromSubjectChoice(parent, parentName, request);

	    Contact contact = contactRepository.findByEntityTypeAndEntityId(EntityType.PARENTS, parent.getId())
	        .orElseThrow(() -> new EntityNotFoundException(parentName + "'s contact not found"));
	    String phone = (parentName == ParentName.FATHER) ? request.fatherPhoneNumber() : request.motherPhoneNumber();
	    contactFactory.updateContact(contact, phone);

	    Address address = addressRepository.findByEntityTypeAndEntityId(EntityType.PARENTS, parent.getId())
	        .orElseThrow(() -> new EntityNotFoundException(parentName + "'s address not found"));
	    String addr = (parentName == ParentName.FATHER) ? request.fatherAddress() : request.motherAddress();
	    addressFactory.updateAddress(address, addr);
	}

	@Override
	public ApiResponse getSubjectChoiceForm() {
		Long userId = userUtil.getCurrentUserInternal().userId();
	    Student student = studentRepository.findByUserId(userId);

	    if (student == null || student.getSubjectChoice() == null) {
	        throw new EntityNotFoundException("Subject choice form not found");
	    }

	    SubjectChoice subjectChoice = student.getSubjectChoice();
	    
	    MatriculationExamDetail medForm = student.getMatriculationExamDetail();
	    Parent father = ParentResolver.resolve(student.getParents(),ParentName.FATHER);
	    Parent mother = ParentResolver.resolve(student.getParents(),ParentName.MOTHER);
	    Contact fatherContact = contactRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,father.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Father's job not found"));
	    Contact motherContact = contactRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,mother.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Mother's job not found"));
	    Address fatherAddr = addressRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,father.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Father's address not found"));
	    Address motherAddr = addressRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,mother.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Mother's address not found"));
	    
	    Job fatherJob = jobRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,father.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Father's job not found"));
	    Job motherJob = jobRepository.findByEntityTypeAndEntityId(EntityType.PARENTS,mother.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Mother's job not found"));
	    Contact studentContact = contactRepository.findByEntityTypeAndEntityId(EntityType.STUDENT,student.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Student's contact number not found"));
	    
	    List<SubjectScoreResponse> subjectScoreResponses = medForm.getSubjectExams()
											    	    .stream()
											    	    .map(se -> SubjectScoreResponse.builder()
											    	        .subjectName(se.getSubject().getName().getDisplayName())
											    	        .score(se.getScore())
											    	        .build())
											    	    .toList();
	    
	    List<MajorChoiceResponse> majorChoiceResponses = subjectChoice.getMajorSubjectChoices()
												        .stream()
												        .map(msc -> MajorChoiceResponse.builder()
												            .majorName(msc.getMajor().getName().name())
												            .priorityScore(msc.getPriorityScore().getValue()) 
												            .build())
												        .toList();
	    
	    Form formData = subjectChoice.getForm();
	    SubjectChoiceResponse response = buildSubjectChoiceResponse(
	            formData, student, medForm,
	            father,mother, fatherJob,motherJob,fatherContact,motherContact,studentContact,
	            fatherAddr,motherAddr,subjectChoice,majorChoiceResponses,subjectScoreResponses,modelMapper);

	    return ApiResponse.builder()
	        .success(1)
	        .code(HttpStatus.OK.value())
	        .message("Subject Choice Form retrieved successfully.")
	        .data(response)
	        .build();
	}
	
	public SubjectChoiceResponse buildSubjectChoiceResponse(
	        Form formData,
	        Student student,
	        MatriculationExamDetail medForm,
	        Parent father,
	        Parent mother,
	        Job fatherJob,
	        Job motherJob,
	        Contact fatherContact,
	        Contact motherContact,
	        Contact studentContact,
	        Address fatherAddr,
	        Address motherAddr,
	        SubjectChoice subjectChoice,
	        List<MajorChoiceResponse> majorChoiceResponses,
	        List<SubjectScoreResponse> subjectScoreResponses,
	        ModelMapper modelMapper) {
	    return SubjectChoiceResponse.builder()
	            .formData(modelMapper.map(formData, FormResponse.class))
	            .enrollmentNumber(student.getEnrollmentNumber())
	            
	            .studentNameEng(student.getEngName())
	            .studentNameMm(student.getMmName())
	            .studentNickname(student.getNickname())
	            
	            .fatherNameEng(father.getEngName())
	            .fatherNameMm(father.getMmName())
	            .fatherNickname(father.getNickname())
	            
	            .motherNameEng(mother.getEngName())
	            .motherNameMm(mother.getMmName())
	            .motherNickname(mother.getNickname())
	            
	            .studentNrc(student.getNrc())
	            .fatherNrc(father.getNrc())
	            .motherNrc(mother.getNrc())
	            
	            .studentEthnicity(student.getEthnicity())
	            .fatherEthnicity(father.getEthnicity())
	            .motherEthnicity(mother.getEthnicity())
	            
	            .studentReligion(student.getReligion())
	            .fatherReligion(father.getReligion())
	            .motherReligion(mother.getReligion())
	            
	            .studentDob(student.getDob())
	            .fatherDob(father.getDob())
	            .motherDob(mother.getDob())
	            .studentPob(student.getPob())
	            .fatherPob(father.getPob())
	            .motherPob(mother.getPob())
	            
	            .studentPhoneNumber(studentContact.getContactNumber())
	            .fatherPhoneNumber(fatherContact.getContactNumber())
	            .motherPhoneNumber(motherContact.getContactNumber())
	            
	            .fatherAddress(fatherAddr.getAddress())
	            .motherAddress(motherAddr.getAddress())
	            
	            .fatherJob(fatherJob.getName())
	            .motherJob(motherJob.getName())
	            
	            .matriculationRollNumber(medForm.getRollNumber())
	            .department(medForm.getDepartment())
	            .matriculationPassedYear(medForm.getYear())
	            
	            .studentSignatureUrl(subjectChoice.getSignatureUrl())
	            .studentSignatureDate(subjectChoice.getSignatureDate())
	            .guardianName(subjectChoice.getGuardianName())
	            .guardianSginatureUrl(subjectChoice.getGuardianSignatureUrl())
	            .guardianSignatureDate(subjectChoice.getGuardianSignatureDate())
	            .majorChoices(majorChoiceResponses)
	            .subjectScores(subjectScoreResponses)
	            .build();
	}

	@Override
	@Transactional
	public ApiResponse updateForRegistratinForm(RegistrationFormRequest request) {
		Long userId = userUtil.getCurrentUserInternal().userId();

        Student student = studentRepository.findByUserId(userId);
        
        if(student==null) {
        	throw new EntityNotFoundException("Student entity not found");
        }
        
        Parent father = parentRepository.findByStudentIdAndParentType_Name(student.getId(),ParentName.FATHER)
    	        .orElseThrow(() -> new EntityNotFoundException("Father entity not found"));
        parentFactory.updateParentFromRegistrationForm(father, ParentName.FATHER,request);
        
        Parent mother = parentRepository.findByStudentIdAndParentType_Name(student.getId(),ParentName.MOTHER)
    	        .orElseThrow(() -> new EntityNotFoundException("Mother entity not found"));
        parentFactory.updateParentFromRegistrationForm(mother, ParentName.MOTHER,request);
        if(request.siblings()!=null) {
        	if(student.getSiblings() != null) siblingRepository.deleteByStudentId(student.getId());
        	List<Sibling> siblings = request.siblings().stream().map(s->{
        		Sibling sibling = new Sibling();
        		sibling.setName(s.name());
            	sibling.setNrc(s.nrc());
            	sibling.setAddress(s.address());
            	sibling.setJob(s.job());
            	sibling.assignStudent(student);
            	return sibling;
        	}).toList();
            siblingRepository.saveAll(siblings);
        }
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Updation for Registration Form is successfully.")
                .data(true)
                .build();
	}
}
