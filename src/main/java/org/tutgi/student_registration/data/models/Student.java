package org.tutgi.student_registration.data.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.tutgi.student_registration.data.models.education.MatriculationExamDetail;
import org.tutgi.student_registration.data.models.education.SubjectChoice;
import org.tutgi.student_registration.data.models.entity.MasterData;
import org.tutgi.student_registration.data.models.form.Acknowledgement;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.models.form.RegistrationForm;
import org.tutgi.student_registration.data.models.lookup.Major;
import org.tutgi.student_registration.data.models.personal.Parent;
import org.tutgi.student_registration.data.models.personal.Sibling;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "students")
@ToString(exclude = {"siblings", "parents", "entranceForm", "registrationForm", "acknowledgement", "subjectChoice","matriculationExamDetail"})
public class Student extends MasterData{
	@Column(nullable = false, name="enrollment_number")
    private String enrollmentNumber;
	
    @Column(nullable = false, name="name_mm")
    private String mmName;

    @Column(nullable = false, name="name_eng")
    private String engName;
    
    @Column
    private String nickname;
    
    @Column
    private String nrc;
    
    @Column
    private String ethnicity;
    
    @Column
    private String religion;
    
    @Column
    private String pob;
    
    @Column
    private LocalDate dob;
    
    @Column(name="photo_url")
    private String photoUrl;
    
    @Column(name="payment_url")
    private String paymentUrl;
    
    @Column(nullable = false)
    private boolean submitted = false;
    
    @Column(nullable = false)
    private boolean isPaid = false;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToOne
    @JoinColumn(name = "major_id")
    private Major major;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Sibling> siblings = new HashSet<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Parent> parents = new HashSet<>();

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private EntranceForm entranceForm;
    
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private RegistrationForm registrationForm;
    
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Acknowledgement acknowledgement;
    
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private SubjectChoice subjectChoice;
    
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private MatriculationExamDetail matriculationExamDetail;
    
    public Student(String mmName, String engName,String nickname, String nrc,String ethnicity,String religion,String pob,LocalDate dob,User user) {
        this.mmName = mmName;
        this.engName = engName;
        this.nrc = nrc;
        this.nickname = nickname;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.pob = pob;
        this.dob = dob;
        this.user = user;
    }
    
    public void updatePersonalInfo(String enrollmentNumber,String engName, String mmName, String nrc, String ethnicity, String religion, LocalDate dob) {
        this.enrollmentNumber = enrollmentNumber;
    	this.engName = engName;
        this.mmName = mmName;
        this.nrc = nrc;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.dob = dob;
    }
    
    public void addSibling(Sibling sibling) {
        if (sibling == null) return;
        siblings.add(sibling);
        sibling.setStudent(this);
    }
    
    public void addParent(Parent parent) {
        if (parent == null) return;
        parents.add(parent);
        parent.setStudent(this);
    }
}
