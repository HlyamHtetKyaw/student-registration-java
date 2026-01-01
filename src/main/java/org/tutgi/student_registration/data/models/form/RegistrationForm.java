package org.tutgi.student_registration.data.models.form;

import java.time.LocalDate;
import java.time.Year;

import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "registration_form", indexes = {
	    @Index(name = "idx_student_id", columnList = "student_id")
	})
public class RegistrationForm extends MasterData{
	@Column(nullable = false, name="class_year")
	private String classYear;

    @Column(nullable = false, name="roll_no")
    private String rollNumber;
    
    @Column(nullable = false,name="signature")
    private String signatureUrl;
    
    @Column(name="guardian_name")
    private String guardianName;
    
    @Column(name="guardian_signature")
    private String guardianSignatureUrl;
    
    @Column(name="docx_url")
    private String docxUrl;
    
    @OneToOne(optional=false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    public RegistrationForm(String classYear, String rollNumber, String signatureUrl,String guardianName,
    		String guardianSignatureUrl,Student student) {
        this.classYear = classYear;
        this.rollNumber = rollNumber;
        this.signatureUrl = signatureUrl;
        this.guardianName = guardianName;
        this.guardianSignatureUrl = guardianSignatureUrl;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if(student != null && student.getRegistrationForm()!=this) {
        	student.setRegistrationForm(this);
        }
    }
}
