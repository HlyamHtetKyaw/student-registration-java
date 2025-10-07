package org.tutgi.student_registration.data.models.form;

import java.time.LocalDate;

import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "acknowledgement", indexes = {
	    @Index(name = "idx_student_id", columnList = "student_id")
	})
public class Acknowledgement extends MasterData{
    @Column(nullable = false,name="signature")
    private String signatureUrl;
    
    @Column(name="signature_date")
    private LocalDate signatureDate;
    
    @Column(name="guardian_name")
    private String guardianName;
    
    @Column(name="guardian_signature")
    private String guardianSignatureUrl;
    
    @Column(name="guardian_signature_date")
    private LocalDate guardianSignatureDate;
    
    @OneToOne(optional=false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
    
    public Acknowledgement(String signatureUrl, LocalDate signatureDate, String guardianName,
    		String guardianSignatureUrl,Student student) {
        this.signatureUrl = signatureUrl;
        this.signatureDate = signatureDate;
        this.guardianName = guardianName;
        this.guardianSignatureUrl = guardianSignatureUrl;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if(student != null && student.getAcknowledgement()!=this) {
        	student.setAcknowledgement(this);
        }
    }
}
