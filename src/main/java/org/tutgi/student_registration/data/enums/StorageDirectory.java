package org.tutgi.student_registration.data.enums;

public enum StorageDirectory {
    PROFILE_PICTURES("profile-pictures"),
	SIGNATURE_PICTURES("signature-pictures"),
	STUDENT_SIGNATURE("student-signatures"),
	GUARDIAN_SIGNATURE("guardian-signatures"),
	STUDENT_PICTURES("student-pictures"),
	PAYMENT("payment"),
	ENTRANCE_FORM("entrance-forms"),
	
	ENTRANCE_FORM_TEMPLATE("shell/EntranceForm.docx");
    private final String directoryName;

    StorageDirectory(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }
}

