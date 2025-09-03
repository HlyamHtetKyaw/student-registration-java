package org.tutgi.student_registration.data.models.education;

import java.util.ArrayList;
import java.util.List;

import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
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
@Table(name = "matriculation_exam_detail", indexes = {
	    @Index(name = "idx_student_id", columnList = "student_id")
	})
public class MatriculationExamDetail extends MasterData{
	@Column(nullable = false, name="roll_no")
    private String rollNumber;
	
	@Column(nullable = false, name="department")
    private String department;
	
	@Column(nullable = false, name="year")
	private String year;
	
	@Column(nullable = false, name="total_score")
	private Long totalScore;
	
	@OneToMany(mappedBy = "med", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SubjectExam> subjectExams = new ArrayList<>();

    @OneToOne(optional=false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    public MatriculationExamDetail(String rollNumber, String department, String year,
    		Long totalScore,Student student) {
        this.rollNumber = rollNumber;
        this.department = department;
        this.year = year;
        this.totalScore = totalScore;
        this.student = student;
    }
    
    public void assignStudent(Student student) {
        this.student = student;
        if(student != null && student.getMatriculationExamDetail()!=this) {
        	student.setMatriculationExamDetail(this);
        }
    }
    
    public void addMajorSubjectChoice(SubjectExam subjectExam) {
        if (subjectExam != null) {
        	subjectExams.add(subjectExam);
        	subjectExam.setMed(this);
        }
    }

    public void removeMajorSubjectChoice(SubjectExam subjectExam) {
        if (subjectExam != null) {
        	subjectExams.remove(subjectExam);
        	subjectExam.setMed(null);
        }
    }
}
