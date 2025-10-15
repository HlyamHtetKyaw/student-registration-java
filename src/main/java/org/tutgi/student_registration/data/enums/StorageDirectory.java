package org.tutgi.student_registration.data.enums;

public enum StorageDirectory {
    PROFILE_PICTURES("profile-pictures"),
	SIGNATURE_PICTURES("signature-pictures"),
	STUDENT_SIGNATURE("student-signatures"),
	GUARDIAN_SIGNATURE("guardian-signatures"),
	STUDENT_PICTURES("student-pictures");
	
    private final String directoryName;

    StorageDirectory(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}

