package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "Visit", description = "A booking for a vet visit.")
@JsonTypeName("Visit")
@Data
public class VisitDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Valid
    @Schema(name = "date", example = "2013-01-01", description = "The date of the visit.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Nullable LocalDate date;

    @NotNull
    @Size(min = 1, max = 255)
    @Schema(name = "description", example = "rabies shot", description = "The description for the visit.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Min(0)
    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the visit.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @NotNull
    @Min(0)
    @Schema(name = "petId", example = "1", description = "The ID of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer petId;
}
