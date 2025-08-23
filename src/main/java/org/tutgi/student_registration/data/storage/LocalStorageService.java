package org.tutgi.student_registration.data.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public String store(final MultipartFile file, final StorageDirectory storageDirecotry,final String username,final Long userId) {
        try {
        	String safeUsername = username.replaceAll("[^a-zA-Z0-9]", "_");
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            
            String filename = userId + "_" + safeUsername + "." + extension;

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
    public Path load(final String folderName,final String filename) {
        return this.rootLocation.resolve(folderName).resolve(filename).normalize();
    }

    @Override
    public Resource loadAsResource(final String folderName,final String filename) {
        try {
            final Path file = load(folderName,filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(final String folderName,final String filename) {
        try {
            final Path file = load(folderName,filename);
            Files.deleteIfExists(file);
            log.info("Deleted file locally: {}", filename);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        log.info("Deleted all local storage files.");
    }

    @Override
    public String update(MultipartFile newFile, String publicId, String folderName) {
        return "";
    }
}
