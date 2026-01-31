package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "Specialty", description = "Fields of specialty of vets.")
@JsonTypeName("Specialty")
@Data
public class SpecialtyDto {

    @Min(0)
    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the specialty.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 80)
    @Schema(name = "name", example = "radiology", description = "The name of the specialty.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
