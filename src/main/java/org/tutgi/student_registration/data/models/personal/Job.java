package org.tutgi.student_registration.data.models.personal;

import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.enums.converter.EntityTypeConverter;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "jobs", indexes = {
    @Index(name = "idx_entity_type_id", columnList = "entity_type, entity_id")
})
public class Job extends MasterData {

    @Column(name = "contact_no")
    private String name;

    @Column(name = "entity_type", nullable = false)
    @Convert(converter = EntityTypeConverter.class)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    public Job(final String name, final EntityType entityType, final Long entityId) {
        this.name = name;
        this.entityType = entityType;
        this.entityId = entityId;
    }
}
