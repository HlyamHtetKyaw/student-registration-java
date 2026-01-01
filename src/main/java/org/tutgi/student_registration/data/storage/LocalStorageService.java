package org.tutgi.student_registration.data.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.data.enums.StorageDirectory;

import lombok.extern.slf4j.Slf4j;

@Service("localStorageService")
@Slf4j
public class LocalStorageService implements StorageService {

    private final Path rootLocation;

    public LocalStorageService() {
        this.rootLocation = Paths.get("upload");
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(this.rootLocation);
            log.info("Initialized local storage at: {}", this.rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize local storage", e);
        }
    }

    @Override
    public String store(final MultipartFile file, final StorageDirectory storageDirecotry) {
        try {
//        	String safeUsername = username.replaceAll("[^a-zA-Z0-9]", "_");
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            
            String filename = System.currentTimeMillis() + "_" + UUID.randomUUID() + "." + extension;

            final Path directoryPath = this.rootLocation.resolve(storageDirecotry.getDirectoryName());

            Files.createDirectories(directoryPath);
            
            final Path destinationFile = directoryPath.resolve(filename).normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("Stored file locally: {}", filename);
                return storageDirecotry.getDirectoryName() + "/" + filename;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }


    @Override
    public List<Path> loadAll() {
        try (Stream<Path> stream = Files.walk(this.rootLocation, 1)) {
            return stream
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(final String filepath) {
        return this.rootLocation.resolve(filepath).normalize();
    }

    @Override
    public Resource loadAsResource(final String filepath) {
        try {
            final Path file = load(filepath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filepath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filepath, e);
        }
    }
    
    @Override
    public byte[] loadFileAsBytes(String filepath) {
        try {
            Path file = load(filepath);
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file as bytes: " + filepath, e);
        }
    }


    @Override
    public void delete(final String filepath) {
        try {
            final Path file = load(filepath);
            Files.deleteIfExists(file);
            log.info("Deleted file locally: {}", filepath);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filepath, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        log.info("Deleted all local storage files.");
    }

    @Override
    public String update(final MultipartFile newFile,final String existingFilepath,final StorageDirectory storageDirectory) {
    	delete(existingFilepath);
    	return store(newFile,storageDirectory);
    }
    
    @Override
    public String update(byte[] newData, String oldFilePath, String baseName,  StorageDirectory storageDirectory) {
        delete(oldFilePath);
        return store(newData, baseName, storageDirectory);
    }

    @Override
    public String store(byte[] data, String baseName, StorageDirectory storageDirectory) {
        try {
            String filename = System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + baseName + "." + "docx";

            final Path directoryPath = this.rootLocation.resolve(storageDirectory.getDirectoryName());
            Files.createDirectories(directoryPath);

            final Path destinationFile = directoryPath.resolve(filename).normalize().toAbsolutePath();

            Files.write(destinationFile, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("Stored generated file locally: {}", filename);

            return storageDirectory.getDirectoryName() + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store generated file.", e);
        }
    }
    
    @Override
    public boolean exists(String filepath) {
        try {
            Path file = load(filepath);
            return Files.exists(file) && Files.isReadable(file);
        } catch (Exception e) {
            return false;
        }
    }

}
