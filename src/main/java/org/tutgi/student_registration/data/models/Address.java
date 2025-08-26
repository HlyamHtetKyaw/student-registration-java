package org.tutgi.student_registration.data.models;

import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
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
public class Address extends MasterData {

    @Lob
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    public Address(final String address, final EntityType entityType, final Long entityId) {
        this.address = address;
        this.entityType = entityType;
        this.entityId = entityId;
    }
}

