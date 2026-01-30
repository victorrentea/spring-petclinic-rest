package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
public class OwnerDto {
    @Min(value = 0)
    @Nullable
    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the pet owner.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @NotNull
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}$")
    @Size(min = 1, max = 30)
    @Schema(name = "firstName", example = "George", description = "The first name of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}\\.?$")
    @Size(min = 1, max = 30)
    @Schema(name = "lastName", example = "Franklin", description = "The last name of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)

    private String lastName;
    @NotNull
    @Size(min = 1, max = 255)
    @Schema(name = "address", example = "110 W. Liberty St.", description = "The postal address of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotNull
    @Size(min = 1, max = 80)
    @Schema(name = "city", example = "Madison", description = "The city of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 1, max = 20)
    @Schema(name = "telephone", example = "6085551023", description = "The telephone number of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telephone;


    @Valid
    @Schema(name = "pets", accessMode = Schema.AccessMode.READ_ONLY, description = "The pets owned by this individual including any booked vet visits.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PetDto> pets = new ArrayList<>();

    public OwnerDto addPetsItem(PetDto petsItem) {
        pets.add(petsItem);
        return this;
    }
}
