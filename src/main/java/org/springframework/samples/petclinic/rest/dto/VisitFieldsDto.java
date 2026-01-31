package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "VisitFields", description = "Editable fields of a vet visit.")
@JsonTypeName("VisitFields")
@Data
public class VisitFieldsDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Valid
    @Schema(name = "date", example = "2013-01-01", description = "The date of the visit.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Nullable LocalDate date;

    @NotNull
    @Size(min = 1, max = 255)
    @Schema(name = "description", example = "rabies shot", description = "The description for the visit.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
}
