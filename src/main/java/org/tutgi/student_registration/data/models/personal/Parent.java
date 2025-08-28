package org.tutgi.student_registration.data.models.personal;

import java.time.LocalDate;
import java.util.List;

import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.entity.MasterData;
import org.tutgi.student_registration.data.models.lookup.ParentType;
import org.tutgi.student_registration.data.repositories.AddressRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "parents")
public class Parent extends MasterData{
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
    
    @Column(name="death_date")
    private LocalDate deathDate;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private ParentType parentType;
    
    public Parent(String mmName, String engName,String nickname, String nrc,String ethnicity,String religion,String pob,LocalDate dob,Student student) {
        this.mmName = mmName;
        this.engName = engName;
        this.nrc = nrc;
        this.nickname = nickname;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.pob = pob;
        this.dob = dob;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if (student != null && !student.getParents().contains(this)) {
            student.getParents().add(this);
        }
    }
    
    @Transient
    public List<Address> getAddresses(AddressRepository addressRepository) {
        return addressRepository.findByEntityTypeAndEntityId(EntityType.PARENTS, this.getId());
    }
}