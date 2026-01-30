package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PetDto {
    @NotNull
    @Size(max = 30)
    @Schema(name = "name", example = "Leo", description = "The name of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Valid
    @Schema(name = "birthDate", example = "2010-09-07", description = "The date of birth of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotNull
    @Valid
    @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("type")
    private PetTypeDto type;

    @Min(value = 0)
    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Min(value = 0)
    @Schema(name = "ownerId", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the pet's owner.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Nullable Integer ownerId;

    @Valid
    @Schema(name = "visits", accessMode = Schema.AccessMode.READ_ONLY, description = "Vet visit bookings for this pet.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<VisitDto> visits = new ArrayList<>();

    public PetDto addVisitsItem(VisitDto visitsItem) {
        if (this.visits == null) {
            this.visits = new ArrayList<>();
        }
        this.visits.add(visitsItem);
        return this;
    }
}
