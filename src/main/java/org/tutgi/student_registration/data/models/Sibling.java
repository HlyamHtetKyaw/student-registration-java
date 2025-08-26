package org.tutgi.student_registration.data.models;

import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "siblings")
public class Sibling extends MasterData{
    @Column(nullable = false)
    private String name;
    
    @Column
    private String nrc;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    public Sibling(String name, String nrc,Student student) {
        this.name = name;
        this.nrc = nrc;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if (!student.getSiblings().contains(this)) {
            student.getSiblings().add(this);
        }
    }
}
