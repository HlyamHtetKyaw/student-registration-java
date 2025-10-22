package org.tutgi.student_registration.data.docsUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//	private void replacePlaceholderWithImage(WordprocessingMLPackage wordMLPackage, String placeholder,
//			byte[] imageBytes, int width, int height) throws Exception {
//
//		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
//
//		List<Object> texts = documentPart.getJAXBNodesViaXPath("//w:t", true);
//
//		for (Object obj : texts) {
//			Text textElement = (Text) ((JAXBElement<?>) obj).getValue();
//			if (textElement.getValue().equals("${" + placeholder + "}")) {
//				BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, imageBytes);
//
//				Inline inline = imagePart.createImageInline("Photo", "Image", 0, 1, width, height, false);
//
//				R run = (R) org.docx4j.jaxb.Context.getWmlObjectFactory().createR();
//				Drawing drawing = org.docx4j.jaxb.Context.getWmlObjectFactory().createDrawing();
//				run.getContent().add(drawing);
//				drawing.getAnchorOrInline().add(inline);
//
//				org.docx4j.wml.R parentRun = (org.docx4j.wml.R) textElement.getParent();
//				parentRun.getContent().clear();
//				parentRun.getContent().add(drawing);
//			}
//		}
//	}

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
				return null; // no recursion needed
			}

			@Override
			public boolean shouldTraverse(Object o) {
				return true;
			}

			@Override
			public void walkJAXBElements(Object parent) {
				List<Object> children = getChildren(parent);
				if (children != null) {
					for (Object child : children) {
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