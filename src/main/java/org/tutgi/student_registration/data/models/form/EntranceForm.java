package org.tutgi.student_registration.data.models.form;

import java.time.LocalDate;

import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "entrance_form", indexes = {
	    @Index(name = "idx_student_id", columnList = "student_id")
	})
public class EntranceForm extends MasterData{
	@Lob
    @Column(nullable = false, name="permanent_address",columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(nullable = false, name="permanent_contact_no")
    private String permanentContactNumber;
    
    @Column(nullable = false,name="signature")
    private String signatureUrl;
    
    @Lob
    @Column(name="student_affair_note",columnDefinition = "TEXT")
    private String studentAffairNote;
    
    @Lob
    @Column(name="student_affair_other_note",columnDefinition = "TEXT")
    private String studentAffairOtherNote;
    
    @Column(name="student_affair_verified_date")
    private LocalDate studentAffairVerifiedDate;
    
    @Lob
    @Column(name="finance_note",columnDefinition = "TEXT")
    private String financeNote;
    
    @Column(name="finance_date")
    private LocalDate financeDate;
    
    @Column(name = "finance_voucher_number", length = 50, unique = true)
    private String financeVoucherNumber;

    @OneToOne(optional=false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "verifier_id")
    private Profile profile;
    
    public EntranceForm(String permanentAddress, String permanentContactNumber, String signatureUrl,
    		Student student) {
        this.permanentAddress = permanentAddress;
        this.permanentContactNumber = permanentContactNumber;
        this.signatureUrl = signatureUrl;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if(student.getEntranceForm()!=this) {
        	student.setEntranceForm(this);
        }
    }
    
    public void assignProfile(Profile profile) {
    	this.profile = profile;
    }
}
