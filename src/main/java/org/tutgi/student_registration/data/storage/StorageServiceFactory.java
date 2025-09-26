package org.tutgi.student_registration.data.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.StorageType;

@Component
public class StorageServiceFactory {

    private final StorageService localStorageService;
    private final StorageType configuredStorageType;

    @Autowired
    public StorageServiceFactory(
            @Qualifier("localStorageService") final  StorageService localStorageService,
            @Value("${app.storage.type:LOCAL}") final  String storageType) {
        this.localStorageService = localStorageService;
        try {
            this.configuredStorageType = StorageType.valueOf(storageType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for app.storage.type property. Must be 'LOCAL'.", e);
        }
    }

    public StorageService getConfiguredStorageService() {
        return getStorageService(this.configuredStorageType);
    }

    public StorageService getStorageService(final StorageType storageType) {
        if (storageType == null) {
            return localStorageService;
        }
        return switch (storageType) {
            case LOCAL -> localStorageService;
            default -> localStorageService;
        };
    }
}
