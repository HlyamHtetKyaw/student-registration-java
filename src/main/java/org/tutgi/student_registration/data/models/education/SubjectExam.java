package org.tutgi.student_registration.data.models.education;

import org.tutgi.student_registration.data.models.entity.MasterData;
import org.tutgi.student_registration.data.models.lookup.Subject;

import jakarta.persistence.Column;
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
	    name = "subject_exam",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"med_id", "subject_id"})
	    }
	)
public class SubjectExam extends MasterData{
    @ManyToOne(optional=false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @ManyToOne(optional=false)
    @JoinColumn(name = "med_id", nullable = false)
    private MatriculationExamDetail med;
    
    @Column(name = "score", nullable = false)
    private Long score;
    
    public SubjectExam(Subject subject, MatriculationExamDetail med, Long score) {
        this.subject = subject;
        this.med = med;
        this.score = score;
    }
}
