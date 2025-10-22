package org.tutgi.student_registration.data.docsUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.dml.wordprocessingDrawing.Inline;
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
        variables.put("academicYear", getSafeValue(formData.getAcademicYear() != null ? formData.getAcademicYear() : "---------"));

        variables.put("enrollmentNumber", "("+getSafeValue(form.getEnrollmentNumber())+")");
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

        variables.put("studentAffairNote1", "-------------------------");
        variables.put("studentAffairNote2", "-------------------------");
        variables.put("checkedDate", "-------------------------");
        variables.put("financeNote", "-------------------------");
        variables.put("receiptDate", "-------------------------");
        variables.put("receiptNumber", "-------------------------");
        variables.put("financeName", "-------------------------");
        variables.put("financeSignature", "-------------------------");
        
        variables.put("formNumber", getSafeValue(formData.getNumber() != null ? formData.getNumber() : "---------"));

        if(form.getStudentPhotoUrl()!=null) {
        	 byte[] photoBytes = storageService.loadFileAsBytes(form.getStudentPhotoUrl());
             replacePlaceholderWithImage(wordMLPackage, "studentPhoto", photoBytes, 800000, 800000);
             //1 inch=914,400 EMUs 0.9*914400=822960
        }
        
        documentPart.variableReplace(variables);
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        wordMLPackage.save(baos);
        
        return baos.toByteArray();
    }
    
    private void replacePlaceholderWithImage(WordprocessingMLPackage wordMLPackage,
                                             String placeholder,
                                             byte[] imageBytes,
                                             int width,
                                             int height) throws Exception {

        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

        List<Object> texts = documentPart.getJAXBNodesViaXPath("//w:t", true);

        for (Object obj : texts) {
            Text textElement = (Text) ((JAXBElement<?>) obj).getValue();
            if (textElement.getValue().equals("${" + placeholder + "}")) {
                BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, imageBytes);

                Inline inline = imagePart.createImageInline(
                        "Photo",        
                        "Image",
                        0,                 
                        1,                 
                        width,              
                        height,          
                        false
                );

                R run = (R) org.docx4j.jaxb.Context.getWmlObjectFactory().createR();
                Drawing drawing = org.docx4j.jaxb.Context.getWmlObjectFactory().createDrawing();
                run.getContent().add(drawing);
                drawing.getAnchorOrInline().add(inline);

                org.docx4j.wml.R parentRun = (org.docx4j.wml.R) textElement.getParent();
                parentRun.getContent().clear();
                parentRun.getContent().add(drawing);
            }
        }
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