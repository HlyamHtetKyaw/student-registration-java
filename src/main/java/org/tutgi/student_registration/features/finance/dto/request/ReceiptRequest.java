package org.tutgi.student_registration.features.finance.dto.request;

import java.util.List;

import org.tutgi.student_registration.data.enums.YearType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for creating or updating a receipt")
public record ReceiptRequest(

        @Schema(
                description = "Academic year of the receipt. Allowed values: FIRST_YEAR, SECOND_YEAR, THIRD_YEAR, FOURTH_YEAR,FIFTH_YEAR,SIXTH_YEAR.",
                example = "FIRST_YEAR",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        YearType year,

        @Valid
        @Schema(description = "List of data entries for the receipt", requiredMode = Schema.RequiredMode.REQUIRED)
        List<Data> data
) {
    @Schema(description = "Receipt data entry")
    public record Data(
            @Schema(description = "Name of item", example = "Sport Fee")
            @NotNull
            String name,

            @Schema(description = "Amount associated with the entry", example = "150.0")
            @NotNull(message = "Amount must not be null")
            @DecimalMin(value = "0.0", inclusive = true, message = "Amount must not be less than zero")
            Double amount
    ) {}
}