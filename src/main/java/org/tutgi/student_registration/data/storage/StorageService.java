package org.tutgi.student_registration.data.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.data.enums.StorageDirectory;

import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void init();
    String store(MultipartFile file,final StorageDirectory storageDirecotry);
    List<Path> loadAll();
    Path load(final String filepath);
    Resource loadAsResource(final String filepath);
    void delete(final String filepath);
    void deleteAll();
    String update(final MultipartFile newFile,final String existingFilepath,final StorageDirectory storageDirecotry);
    
    String store(byte[] data, String baseName, StorageDirectory storageDirectory);
    String update(byte[] newData, String oldFilePath, String baseName,  StorageDirectory storageDirectory);
}
