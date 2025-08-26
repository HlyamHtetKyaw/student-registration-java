package org.tutgi.student_registration.data.models;

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
@Table(name = "majors")
public class Major extends MasterData{
    @Column(nullable = false, name="name_mm")
    private String mmName;

    @Column(nullable = false, name="name_eng")
    private String engName;
    
    @Column(nullable = false, name="short_name")
    private String shortName;
    
    public Major(String mmName, String engName,String shortName) {
        this.mmName = mmName;
        this.engName = engName;
        this.shortName = shortName;
    }
}
