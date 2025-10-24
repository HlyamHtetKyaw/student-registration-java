package org.tutgi.student_registration.data.docsUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.form.dto.response.FormResponse;
import org.tutgi.student_registration.features.students.dto.response.EntranceFormResponse;
import org.tutgi.student_registration.features.students.dto.response.RegistrationFormResponse;
import org.tutgi.student_registration.features.students.dto.response.RegistrationFormResponse.SiblingResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse.MajorChoiceResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse.SubjectScoreResponse;

import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Docx4jFillerService {
	private final StorageService storageService;

	public byte[] fillEntranceFormTemplate(String templatePath, EntranceFormResponse entranceForm) throws Exception {
		Resource resource = new ClassPathResource(templatePath);
		File templateFile = resource.getFile();
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateFile);
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

		VariablePrepare.prepare(wordMLPackage);

		Map<String, String> variables = new HashMap<>();
		EntranceFormResponse form = entranceForm;

		FormResponse formData = form.getFormData();
		variables.put("formCode", getSafeValue(formData.getCode() != null ? formData.getCode() : "---------"));
		variables.put("academicYear",
				getSafeValue(formData.getAcademicYear() != null ? formData.getAcademicYear() : "---------"));

		variables.put("enrollmentNumber", "(" + getSafeValue(form.getEnrollmentNumber()) + ")");
		variables.put("studentNameMM", getSafeValue(form.getStudentNameMm()));
		variables.put("studentNameEng", getSafeValue(form.getStudentNameEng()));
		variables.put("nrc", getSafeValue(form.getStudentNrc()));
		variables.put("ethnicity", getSafeValue(form.getEthnicity()));
		variables.put("religion", getSafeValue(form.getReligion()));
		variables.put("dob", getSafeValue(form.getDob()));
		variables.put("matYear", getSafeValue(form.getMatriculationPassedYear()));
		variables.put("department", getSafeValue(form.getDepartment()));

		variables.put("fatherNameMM", getSafeValue(form.getFatherNameMm()));
		variables.put("fatherNameEng", getSafeValue(form.getFatherNameEng()));
		variables.put("fatherNrc", getSafeValue(form.getFatherNrc()));
		variables.put("fatherJob", getSafeValue(form.getFatherJob()));

		variables.put("motherNameMM", getSafeValue(form.getMotherNameMm()));
		variables.put("motherNameEng", getSafeValue(form.getMotherNameEng()));
		variables.put("motherNrc", getSafeValue(form.getMotherNrc()));
		variables.put("motherJob", getSafeValue(form.getMotherJob()));

		variables.put("address", getSafeValue(form.getAddress()));
		variables.put("phone", getSafeValue(form.getPhoneNumber()));
		variables.put("pAddress", getSafeValue(form.getPermanentAddress()));
		variables.put("pPhone", getSafeValue(form.getPermanentPhoneNumber()));

		variables.put("studentAffairNote1", getSafeValue(form.getDepartmentSection().getStudentAffairNote()));
		variables.put("studentAffairNote2", getSafeValue(form.getDepartmentSection().getStudentAffairOtherNote()));
		variables.put("checkedDate", getSafeValue(form.getDepartmentSection().getStudentAffairVerifiedDate()));
		variables.put("financeNote", getSafeValue(form.getDepartmentSection().getFinanceNote()));
		variables.put("receiptDate", getSafeValue(form.getDepartmentSection().getFinanceDate()));
		variables.put("receiptNumber", getSafeValue(form.getDepartmentSection().getFinanceVoucherNumber()));
		variables.put("financeName", getSafeValue(form.getDepartmentSection().getFinanceVerifierName()));

		variables.put("formNumber", getSafeValue(formData.getNumber()));

		// 1 inch=914,400 EMUs 0.9*914400=822960
		// Replace image placeholders first (before variableReplace)
		if(form.getStudentPhotoUrl() != null) {
		    byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentPhotoUrl());
		    replacePlaceholderWithImage(wordMLPackage, "studentPhoto", photoBytes, 800000, 800000);
		    variables.remove("studentPhoto");
		}

		if(form.getStudentSignatureUrl() != null) {
		    byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentSignatureUrl());
		    replacePlaceholderWithImage(wordMLPackage, "studentSignature", photoBytes, 914400, 228600);
		    variables.remove("studentSignature");
		}

		if(form.getDepartmentSection() != null && form.getDepartmentSection().getFinanceVerifierSignature() != null) {
		    byte[] photoBytes = storageService.loadFileAsBytes(form.getDepartmentSection().getFinanceVerifierSignature());
		    replacePlaceholderWithImage(wordMLPackage, "financeSignature", photoBytes, 914400, 228600);
		    variables.remove("financeSignature");
		}

		documentPart.variableReplace(variables);

		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		wordMLPackage.save(baos);

		return baos.toByteArray();
	}
	
	public byte[] fillSubjectChoiceTemplate(String templatePath, SubjectChoiceResponse entranceForm) throws Exception {
		Resource resource = new ClassPathResource(templatePath);
		File templateFile = resource.getFile();
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateFile);
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

		VariablePrepare.prepare(wordMLPackage);

		Map<String, String> variables = new HashMap<>();
		SubjectChoiceResponse form = entranceForm;
		FormResponse formData = form.getFormData();
		variables.put("formCode", getSafeValue(formData.getCode() != null ? formData.getCode() : "---------"));
		variables.put("academicYear",
				getSafeValue(formData.getAcademicYear() != null ? formData.getAcademicYear() : "---------"));
		variables.put("formNumber", getSafeValue(formData.getNumber()));
		variables.put("enrollmentNumber", getSafeValue(form.getEnrollmentNumber()));
		variables.put("studentNameMM", getSafeValue(form.getStudentNameMm()));
		variables.put("fatherNameMM", getSafeValue(form.getFatherNameMm()));
		variables.put("motherNameMM", getSafeValue(form.getMotherNameMm()));
		variables.put("studentNickname", getSafeValue(form.getStudentNickname()));
		variables.put("fatherNickname", getSafeValue(form.getFatherNickname()));
		variables.put("motherNickname", getSafeValue(form.getMotherNickname()));
		variables.put("studentNrc", getSafeValue(form.getStudentNrc()));
		variables.put("fatherNrc", getSafeValue(form.getFatherNrc()));
		variables.put("motherNrc", getSafeValue(form.getMotherNrc()));
		variables.put("studentEthnicity", getSafeValue(form.getStudentEthnicity()));
		variables.put("fatherEthnicity", getSafeValue(form.getFatherEthnicity()));
		variables.put("motherEthnicity", getSafeValue(form.getMotherEthnicity()));
		variables.put("studentReligion", getSafeValue(form.getStudentReligion()));
		variables.put("fatherReligion", getSafeValue(form.getFatherReligion()));
		variables.put("motherReligion", getSafeValue(form.getMotherReligion()));
		variables.put("studentPob", getSafeValue(form.getStudentPob()));
		variables.put("fatherPob", getSafeValue(form.getFatherPob()));
		variables.put("motherPob", getSafeValue(form.getMotherPob()));
		variables.put("studentDob", getSafeValue(form.getStudentDob()));
		variables.put("fatherDob", getSafeValue(form.getFatherDob()));
		variables.put("motherDob", getSafeValue(form.getMotherDob()));
		variables.put("studentPh", getSafeValue(form.getStudentPhoneNumber()));
		variables.put("fatherPh", getSafeValue(form.getFatherPhoneNumber()));
		variables.put("motherPh", getSafeValue(form.getMotherPhoneNumber()));
		variables.put("fatherJob", getSafeValue(form.getFatherJob()));
		variables.put("fatherAddress", getSafeValue(form.getFatherAddress()));
		variables.put("motherJob", getSafeValue(form.getMotherJob()));
		variables.put("motherAddress", getSafeValue(form.getMotherAddress()));

		variables.put("matRollNumber", getSafeValue(form.getMatriculationRollNumber()));
		long total = 0L;

		if (form.getSubjectScores() != null && !form.getSubjectScores().isEmpty()) {
		    for (SubjectScoreResponse se : form.getSubjectScores()) {
		        String displayName = se.subjectName();
		        Long score = se.score();

		        if (score != null) total += score;
		        if (displayName == null) continue;

		        switch (displayName.trim()) {
		            case "မြန်မာစာ" -> variables.put("mmMark", getSafeValue(score));
		            case "အင်္ဂလိပ်စာ" -> variables.put("engMark", getSafeValue(score));
		            case "သင်္ချာ" -> variables.put("mathMark", getSafeValue(score));
		            case "ဓါတု" -> variables.put("chemistMark", getSafeValue(score));
		            case "ရူပ" -> variables.put("physicsMark", getSafeValue(score));
		            case "ဇီဝ/ဘောဂ/သမိုင်း/ပထဝီ/စိတ်ကြိုက်မြန်မာ" -> variables.put("otherMark", getSafeValue(score));
		            default -> System.out.println("⚠️ Unmapped subject: " + displayName);
		        }
		    }
		}
		variables.put("total", String.valueOf(total));
		
		Map<Integer, String> priorityMap = Map.of(
		    1, "first",
		    2, "second",
		    3, "third",
		    4, "fourth",
		    5, "fifth",
		    6, "sixth"
		);

		if (form.getMajorChoices() != null && !form.getMajorChoices().isEmpty()) {
		    for (MajorChoiceResponse choice : form.getMajorChoices()) {
		        Integer priority = choice.getPriorityScore();
		        String majorName = choice.getMajorName();

		        if (priority != null && majorName != null) {
		            String varKey = priorityMap.get(priority);
		            if (varKey != null) {
		                variables.put(varKey, getSafeValue(majorName));
		            }
		        }
		    }
		}

		for (int i = 1; i <= 6; i++) {
		    String varKey = priorityMap.get(i);
		    variables.putIfAbsent(varKey, "");
		}

		variables.put("studentSignDate", getSafeValue(form.getStudentSignatureDate()));
		variables.put("guardianSignDate", getSafeValue(form.getGuardianSignatureDate()));

		if (form.getStudentPhotoUrl() != null) {
		    byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentPhotoUrl());
		    replacePlaceholderWithImage(wordMLPackage, "studentPhoto", photoBytes, 800000, 800000);
		    variables.remove("studentPhoto");
		}

		if (form.getStudentSignatureUrl() != null) {
		    byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentSignatureUrl());
		    replacePlaceholderWithImage(wordMLPackage, "studentSign", photoBytes, 914400, 228600);
		    variables.remove("studentSign");
		}
		
		if (form.getGuardianSginatureUrl() != null) {
		    byte[] photoBytes = storageService.loadFileAsBytes(form.getGuardianSginatureUrl());
		    replacePlaceholderWithImage(wordMLPackage, "guardianSign", photoBytes, 914400, 228600);
		    variables.remove("guardianSign");
		}
		
		documentPart.variableReplace(variables);

		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		wordMLPackage.save(baos);

		return baos.toByteArray();
	}
	
	public byte[] fillRegistrationFormTemplate(String templatePath, RegistrationFormResponse registrationForm) throws Exception {
	    Resource resource = new ClassPathResource(templatePath);
	    File templateFile = resource.getFile();
	    WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateFile);
	    MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

	    Map<String, Object> data = new HashMap<>();
	    RegistrationFormResponse form = registrationForm;
	    FormResponse formData = form.getFormData();

	    data.put("formNumber", getSafeValue(formData.getNumber()));
	    data.put("academicYear", getSafeValue(formData.getAcademicYear()));
	    data.put("matRollNumber", getSafeValue(form.getMatriculationRollNumber()));
	    data.put("enrollmentNumber", getSafeValue(form.getEnrollmentNumber()));

	    data.put("studentNameMM", getSafeValue(form.getStudentNameMm()));
	    data.put("fatherNameMM", getSafeValue(form.getFatherNameMm()));
	    data.put("motherNameMM", getSafeValue(form.getMotherNameMm()));

	    data.put("studentNameEng", getSafeValue(form.getStudentNameEng()));
	    data.put("fatherNameEng", getSafeValue(form.getFatherNameEng()));
	    data.put("motherNameEng", getSafeValue(form.getMotherNameEng()));

	    data.put("studentNickname", getSafeValue(form.getStudentNickname()));
	    data.put("fatherNickname", getSafeValue(form.getFatherNickname()));
	    data.put("motherNickname", getSafeValue(form.getMotherNickname()));

	    data.put("studentNrc", getSafeValue(form.getStudentNrc()));
	    data.put("fatherNrc", getSafeValue(form.getFatherNrc()));
	    data.put("motherNrc", getSafeValue(form.getMotherNrc()));

	    data.put("studentEthnicity", getSafeValue(form.getStudentEthnicity()));
	    data.put("fatherEthnicity", getSafeValue(form.getFatherEthnicity()));
	    data.put("motherEthnicity", getSafeValue(form.getMotherEthnicity()));

	    data.put("studentReligion", getSafeValue(form.getStudentReligion()));
	    data.put("fatherReligion", getSafeValue(form.getFatherReligion()));
	    data.put("motherReligion", getSafeValue(form.getMotherReligion()));

	    data.put("studentPob", getSafeValue(form.getStudentPob()));
	    data.put("studentDob", getSafeValue(form.getStudentDob()));
	    data.put("fatherPob", getSafeValue(form.getFatherPob()));
	    data.put("fatherDob", getSafeValue(form.getFatherDob()));
	    data.put("motherPob", getSafeValue(form.getMotherPob()));
	    data.put("motherDob", getSafeValue(form.getMotherDob()));

	    data.put("fatherJob", getSafeValue(form.getFatherJob()));
	    data.put("fatherAddress", getSafeValue(form.getFatherAddress()));
	    data.put("motherJob", getSafeValue(form.getMotherJob()));
	    data.put("motherAddress", getSafeValue(form.getMotherAddress()));

	    data.put("fatherDeceasedYear", getSafeValue(form.getFatherDeathDate()));
	    data.put("motherDeceasedYear", getSafeValue(form.getMotherDeathDate()));

	    if (form.getStudentPhotoUrl() != null) {
	        byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentPhotoUrl());
	        data.put("studentPhoto", photoBytes);
	    }
	    if (form.getGuardianSginatureUrl() != null) {
	        byte[] photoBytes = storageService.loadFileAsBytes(form.getGuardianSginatureUrl());
	        data.put("guardianSign", photoBytes);
	    }
	    if (form.getStudentSignatureUrl() != null) {
	        byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentSignatureUrl());
	        data.put("studentSign", photoBytes);
	    }
	    
	    List<Map<String, String>> siblingList = new ArrayList<>();
	    for (SiblingResponse s : form.getSiblings()) {
	        Map<String, String> siblingMap = new HashMap<>();
	        siblingMap.put("name", getSafeValue(s.getName()));
	        siblingMap.put("nrc", getSafeValue(s.getNrc()));
	        siblingMap.put("job", getSafeValue(s.getJob()));
	        siblingMap.put("address", getSafeValue(s.getAddress()));
	        siblingList.add(siblingMap);
	    }
	    data.put("siblings", siblingList);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    wordMLPackage.save(baos);
	    return baos.toByteArray();
	}
	
	private void replacePlaceholderWithImage(WordprocessingMLPackage wordMLPackage, String placeholder,
			byte[] imageBytes, int width, int height) throws Exception {

		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		String fullPlaceholder = "${" + placeholder + "}";

		new TraversalUtil(documentPart, new TraversalUtil.Callback() {
			@Override
			public List<Object> apply(Object o) {
				String normalizedPlaceholder = placeholder.replaceAll("\\s+", "");
				if (o instanceof Text) {
					Text text = (Text) o;
					if (text.getValue() != null && text.getValue().replaceAll("\\s+", "").equals("${" + normalizedPlaceholder + "}")) {
						Object parent = text.getParent();
						if (parent instanceof R) {
							R run = (R) parent;

							try {
								BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
										.createImagePart(wordMLPackage, imageBytes);
								Inline inline = imagePart.createImageInline("Image", "Image", 0, 1, width, height,
										false);

								Drawing drawing = Context.getWmlObjectFactory().createDrawing();
								drawing.getAnchorOrInline().add(inline);

								run.getContent().clear();
								run.getContent().add(drawing);

								System.out.println("✅ Image inserted for placeholder: " + fullPlaceholder);
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException("Failed to insert image", e);
							}
						}
					}
				}
				return null;
			}

			@Override
			public boolean shouldTraverse(Object o) {
				return true;
			}

			@Override
			public void walkJAXBElements(Object parent) {
			    List<Object> children = getChildren(parent);
			    if (children != null) {
			        for (Object child : new ArrayList<>(children)) { 
			            if (child instanceof JAXBElement) {
			                child = ((JAXBElement<?>) child).getValue();
			            }
			            apply(child);
			            walkJAXBElements(child);
			        }
			    }
			}

			public List<Object> getChildren(Object o) {
				try {
					return TraversalUtil.getChildrenImpl(o);
				} catch (Exception e) {
					return null;
				}
			}
		});
	}

	private String getSafeValue(Object value) {
		return value == null ? "-------------------------" : value.toString();
	}

}
//try {
//    byte[] filled = docxFillerService.fillTemplate(StorageDirectory.ENTRANCE_FORM_TEMPLATE.getDirectoryName(), response);
//
//    String storedPath = storageService.store(
//        filled,
//        student.getEngName(),
//        StorageDirectory.ENTRANCE_FORM
//    );
//
//    System.out.println("Generated file saved at: " + storedPath);
////    String updatedPath = storageService.update(filledBytes, oldPath, student.getEngName(), StorageDirectory.ENTRANCE_FORM);
//
//} catch (Exception e) {
//    e.printStackTrace();
//}