package org.tutgi.student_registration.data.models.lookup;

import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subjects")
public class Subject extends MasterData{
    @Column(nullable = false, name="name")
    private String name;
    
    public Subject(String name) {
    	this.name = name;
    }
}
