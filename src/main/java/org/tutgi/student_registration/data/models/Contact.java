package org.tutgi.student_registration.data.models;

import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "addresses", indexes = {
    @Index(name = "idx_entity_type_id", columnList = "entity_type, entity_id")
})
public class Contact extends MasterData {

    @Column(name = "contact_no")
    private String contactNumber;

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    public Contact(final String contactNumber, final EntityType entityType, final Long entityId) {
        this.contactNumber = contactNumber;
        this.entityType = entityType;
        this.entityId = entityId;
    }
}
