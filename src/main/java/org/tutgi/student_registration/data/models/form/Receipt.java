package org.tutgi.student_registration.data.models.form;

import java.util.ArrayList;
import java.util.List;

import org.tutgi.student_registration.data.enums.YearType;
import org.tutgi.student_registration.data.enums.converter.YearTypeConverter;
import org.tutgi.student_registration.data.models.entity.MasterData;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "receipts")
public class Receipt extends MasterData {

    @Convert(converter = YearTypeConverter.class)
    @Column(name = "year")
    private YearType year;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "receipt_data",
            joinColumns = @JoinColumn(name = "receipt_id")
    )
    private List<ReceiptData> data = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "phone_numbers",
            joinColumns = @JoinColumn(name = "phone_number_id")
    )
    private List<PhoneNumbers> phoneNumbers = new ArrayList<>();
}