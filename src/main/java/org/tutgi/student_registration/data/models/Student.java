package org.tutgi.student_registration.data.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.tutgi.student_registration.data.models.entity.MasterData;

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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "students")
public class Student extends MasterData{
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
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToOne
    @JoinColumn(name = "major_id")
    private Major major;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Sibling> siblings = new ArrayList<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Parent> parents = new ArrayList<>();
    
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
    
    public void addSibling(Sibling sibling) {
    	if (sibling == null) return;
    	
        if (!siblings.contains(sibling)) {
            siblings.add(sibling);
        }
        if (sibling.getStudent() != this) {
            sibling.setStudent(this);
        }
    }
    
    public void addParent(Parent parent) {
        if (parent == null) return;

        if (!parents.contains(parent)) {
            parents.add(parent);
        }

        if (parent.getStudent() != this) {
            parent.setStudent(this);
        }
    }
}
