package org.tutgi.student_registration.data.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.data.enums.StorageDirectory;

import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void init();
    String store(MultipartFile file,final StorageDirectory storageDirecotry,final String username,final Long userId);
    List<Path> loadAll();
    Path load(final String folderName,final String filename);
    Resource loadAsResource(final String folderName,final String filename);
    void delete(final String folderName,final String filename);
    void deleteAll();
    String update(MultipartFile newFile, String publicId, String folderName);
}
