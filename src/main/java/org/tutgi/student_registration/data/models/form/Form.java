package org.tutgi.student_registration.data.models.form;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tutgi.student_registration.data.models.entity.MasterData;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "forms")
public class Form extends MasterData {

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "stamp_url")
    private String stampUrl;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen = true;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Acknowledgement> acknowledgements = new ArrayList<>();

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
