package org.tutgi.student_registration.data.models.education;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.entity.MasterData;
import org.tutgi.student_registration.data.models.form.Form;
import org.tutgi.student_registration.data.models.form.MajorSubjectChoiceForm;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subject_choice", indexes = {
	    @Index(name = "idx_student_id", columnList = "student_id")
	})
public class SubjectChoice extends MasterData{
    @Column(name="signature")
    private String signatureUrl;
    
    @Column(name="signature_date")
    private LocalDate signatureDate;
    
    @Column(name="guardian_name")
    private String guardianName;
    
    @Column(name="guardian_signature")
    private String guardianSignatureUrl;
    
    @Column(name="guardian_signature_date")
    private LocalDate guardianSignatureDate;
    
    @OneToMany(mappedBy = "subjectChoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MajorSubjectChoiceForm> majorSubjectChoices = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;
    
    @OneToOne(optional=false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    public SubjectChoice(String signatureUrl, LocalDate signatureDate, String guardianName,
    		String guardianSignatureUrl,Student student) {
        this.signatureUrl = signatureUrl;
        this.signatureDate = signatureDate;
        this.guardianName = guardianName;
        this.guardianSignatureUrl = guardianSignatureUrl;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if(student != null && student.getSubjectChoice()!=this) {
        	student.setSubjectChoice(this);
        }
    }
    
    public void addMajorSubjectChoice(MajorSubjectChoiceForm choice) {
        if (choice != null) {
            majorSubjectChoices.add(choice);
            choice.setSubjectChoice(this);
        }
    }

    public void removeMajorSubjectChoice(MajorSubjectChoiceForm choice) {
        if (choice != null) {
            majorSubjectChoices.remove(choice);
            choice.setSubjectChoice(null);
        }
    }
    
    public void assignForm(Form form) {
    	this.form = form;
    }
}
