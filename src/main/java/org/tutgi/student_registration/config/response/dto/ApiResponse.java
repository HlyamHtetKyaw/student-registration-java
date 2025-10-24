package org.tutgi.student_registration.config.response.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private int success;
    private int code;
    private Map<String, Object> meta;
    private Object data;
    private String message;
    private double duration;
}