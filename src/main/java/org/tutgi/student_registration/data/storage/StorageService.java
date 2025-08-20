package org.tutgi.student_registration.data.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void init();
    String store(MultipartFile file,final String folderName);
    List<Path> loadAll();
    Path load(final String folderName,final String filename);
    Resource loadAsResource(final String folderName,final String filename);
    void delete(final String folderName,final String filename);
    void deleteAll();
    String update(MultipartFile newFile, String publicId, String folderName);
}
