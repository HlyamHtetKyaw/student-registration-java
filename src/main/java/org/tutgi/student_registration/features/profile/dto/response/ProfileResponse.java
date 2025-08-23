package org.tutgi.student_registration.features.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    String mmName;
    String engName;
    String nrc;
    String photoUrl;
    String signatureUrl;
}   
