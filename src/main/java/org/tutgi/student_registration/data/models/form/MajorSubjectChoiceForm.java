package org.tutgi.student_registration.data.models.form;

import org.tutgi.student_registration.data.enums.PriorityScore;
import org.tutgi.student_registration.data.enums.converter.PriorityScoreConverter;
import org.tutgi.student_registration.data.models.education.SubjectChoice;
import org.tutgi.student_registration.data.models.entity.MasterData;
import org.tutgi.student_registration.data.models.lookup.Major;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
	    name = "major_subject_choice_form",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"subject_choice_id", "priority"}),
	        @UniqueConstraint(columnNames = {"subject_choice_id", "major_id"})
	    }
	)

public class MajorSubjectChoiceForm extends MasterData{
	@ManyToOne(optional = false)
	@JoinColumn(name = "major_id", nullable = false)
	private Major major;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "subject_choice_id", nullable = false)
    private SubjectChoice subjectChoice;
    
    @Column(name = "priority", nullable = false)
    @Convert(converter = PriorityScoreConverter.class)
    private PriorityScore priorityScore;
    
    public MajorSubjectChoiceForm(Major major, SubjectChoice subjectChoice, PriorityScore priorityScore) {
        this.major = major;
        this.subjectChoice = subjectChoice;
        this.priorityScore = priorityScore;
    }
}
