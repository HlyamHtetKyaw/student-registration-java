package org.tutgi.student_registration.data.models;

import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "photos")
public class Photo extends MasterData{
    @Column(name="url")
    private String photoUrl;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    public Photo(String photoUrl,Student student) {
        this.photoUrl = photoUrl;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if (!student.getPhotos().contains(this)) {
            student.getPhotos().add(this);
        }
    }
}
