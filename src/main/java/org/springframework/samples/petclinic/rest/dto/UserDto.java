package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * An user.
 */

@Data
public class UserDto {

    @NotNull
    @Size(min = 1, max = 80)
    @Schema(name = "username", example = "john.doe", description = "The username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Size(min = 1, max = 80)
    @Schema(name = "password", example = "1234abc", description = "The password", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Nullable String password;

    @Schema(name = "enabled", example = "true", description = "Indicates if the user is enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Nullable Boolean enabled;

    @Valid
    @Schema(name = "roles", description = "The roles of an user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<@Valid RoleDto> roles = new ArrayList<>();
}

