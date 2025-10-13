package org.tutgi.student_registration.data.models.lookup;

import org.tutgi.student_registration.data.enums.SubjectName;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false, name="name")
    private SubjectName name;

    public Subject(SubjectName name) {
    	this.name = name;
    }
}
