package org.tutgi.student_registration.data.docsUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.data.enums.StorageDirectory;
import org.tutgi.student_registration.features.students.dto.response.EntranceFormResponse;

@Service
public class Docx4jFillerService {

    public byte[] fillTemplate(String templatePath, EntranceFormResponse entranceForm) throws Exception {
    	Resource resource = new ClassPathResource(templatePath);
    	File templateFile = resource.getFile();
    	WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateFile);
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

        VariablePrepare.prepare(wordMLPackage);

        Map<String, String> variables = new HashMap<>();
        EntranceFormResponse form = entranceForm;
        variables.put("studentMMName", form.getStudentNameMm());
        variables.put("studentEngName", form.getStudentNameEng());
        variables.put("ethnicity", form.getEthnicity());
        variables.put("religion", form.getReligion());

        documentPart.variableReplace(variables);

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        wordMLPackage.save(baos);

        return baos.toByteArray();
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