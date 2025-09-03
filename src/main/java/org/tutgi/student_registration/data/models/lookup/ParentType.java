package org.tutgi.student_registration.data.models.lookup;

import org.tutgi.student_registration.data.enums.ParentName;
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
@Table(name="parent_type")
public class ParentType extends MasterData {
	@Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ParentName name;

    public ParentType(final ParentName name) {
        this.name = name;
    }
}
