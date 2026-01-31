package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "PetFields", description = "Editable fields of a pet.")
@Data
public class PetFieldsDto {

    @NotNull
    @Size(max = 30)
    @Schema(name = "name", example = "Leo", description = "The name of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    @Valid
    @Schema(name = "birthDate", example = "2010-09-07", description = "The date of birth of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthDate;

    @NotNull
    @Valid
    @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
    private PetTypeDto type;
}
