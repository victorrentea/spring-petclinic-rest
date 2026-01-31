package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "VetFields", description = "Editable fields of a veterinarian.")
@JsonTypeName("VetFields")
@Data
public class VetFieldsDto {

    @NotNull
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}$")
    @Size(min = 1, max = 30)
    @Schema(name = "firstName", example = "James", description = "The first name of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}\\.?$")
    @Size(min = 1, max = 30)
    @Schema(name = "lastName", example = "Carter", description = "The last name of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotNull
    @Valid
    @Schema(name = "specialties", description = "The specialties of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@Valid SpecialtyDto> specialties = new ArrayList<>();
}
