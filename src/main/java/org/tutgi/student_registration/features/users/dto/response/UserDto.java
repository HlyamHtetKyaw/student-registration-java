package org.tutgi.student_registration.features.users.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private String rollNo;
    private String genderName;
    private String dateFormat;
    private String currencyCode;
    private BigDecimal setAmount;
    private String createdAt;
    private String updatedAt;
}
