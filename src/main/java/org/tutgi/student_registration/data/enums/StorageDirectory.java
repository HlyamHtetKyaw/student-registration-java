package org.tutgi.student_registration.data.enums;

public enum StorageDirectory {
    PROFILE_PICTURES("profile-pictures"),
	SIGNATURE_PICTURES("signature-pictures");
	
    private final String directoryName;

    StorageDirectory(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}

