package org.tutgi.student_registration.data.models.lookup;

import org.tutgi.student_registration.data.enums.MajorName;
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
@Table(name = "majors")
public class Major extends MasterData{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private MajorName name;
    
    public Major(MajorName name) {
        this.name = name;
    }
    
    public String getEngName() {
        return name.getEngName();
    }

    public String getMmName() {
        return name.getMmName();
    }
}
