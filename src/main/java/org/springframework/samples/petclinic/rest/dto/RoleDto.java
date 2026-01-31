package org.springframework.samples.petclinic.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDto {

    @NotNull
    @Size(min = 1, max = 80)
    @Schema(name = "name", example = "admin", description = "The role's name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

}
