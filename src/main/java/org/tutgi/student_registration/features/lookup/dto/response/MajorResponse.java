package org.tutgi.student_registration.features.lookup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MajorResponse {
    private Long id;
    private String shortName;
    private String engName;
    private String mmName;
}

