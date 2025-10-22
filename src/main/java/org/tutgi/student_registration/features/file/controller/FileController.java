package org.tutgi.student_registration.features.file.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tutgi.student_registration.data.enums.FormType;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.file.dto.response.FileResponse;
import org.tutgi.student_registration.features.file.service.FileService;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "File Module", description = "Endpoints for getting form files")
@RestController
@RequestMapping("/${api.base.path}/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
	private final FileService fileService;
	private final StorageService storageService;
	@Operation(summary = "Download form by type for a student")
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFormByType(
	        @RequestParam("type") FormType type,
	        @RequestParam("studentId") Long studentId
	) {
	    FileResponse fileDownloadDto = fileService.downloadFormByType(type, studentId);
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(
	                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
	            ))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDownloadDto.fileName() + "\"")
	            .body(fileDownloadDto.resource());
	}
	
	@GetMapping("/previewBase64")
	public ResponseEntity<Map<String, String>> previewDocxBase64(
	        @RequestParam("type") FormType type,
	        @RequestParam("studentId") Long studentId
	) throws IOException {
	    String filePath = fileService.getFilePathByType(type, studentId);
	    byte[] bytes = storageService.loadFileAsBytes(filePath);
	    String base64Content = Base64.getEncoder().encodeToString(bytes);

	    Map<String, String> response = Map.of(
	        "fileName", "Document.docx",
	        "base64", base64Content
	    );

	    return ResponseEntity.ok(response);
	}
	
    @GetMapping("/previewHtml")
    public ResponseEntity<Map<String, String>> previewDocxAsHtml(
            @RequestParam("type") FormType type,
            @RequestParam("studentId") Long studentId) {

        String filePath = fileService.getFilePathByType(type, studentId);
        String htmlContent = convertDocxToHtml(storageService.load(filePath));

        Map<String, String> response = Map.of(
                "fileName", "preview.html",
                "html", htmlContent
        );

        return ResponseEntity.ok(response);
    }
    
    public String convertDocxToHtml(Path docxPath) {
        try (InputStream is = new FileInputStream(docxPath.toFile())) {
            DocumentConverter converter = new DocumentConverter();
            Result result = converter.convertToHtml(is);
            return (String) result.getValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert DOCX to HTML", e);
        }
    }

}
