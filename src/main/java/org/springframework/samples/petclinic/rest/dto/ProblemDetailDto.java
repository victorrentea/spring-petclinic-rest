package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "ProblemDetail", description = "The schema for all error responses.")
@Data
public class ProblemDetailDto {

    @Valid
    @Schema(name = "type", accessMode = Schema.AccessMode.READ_ONLY, example = "http://localhost:9966/petclinic/api/owner", description = "Full URL that originated the error response.", requiredMode = Schema.RequiredMode.REQUIRED)
    private URI type;

    @Schema(name = "title", accessMode = Schema.AccessMode.READ_ONLY, example = "NoResourceFoundException", description = "The short error title.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Min(400)
    @Max(600)
    @Schema(name = "status", accessMode = Schema.AccessMode.READ_ONLY, example = "500", description = "HTTP status code", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(name = "detail", accessMode = Schema.AccessMode.READ_ONLY, example = "No static resource api/owner.", description = "The long error message.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String detail;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Valid
    @Schema(name = "timestamp", accessMode = Schema.AccessMode.READ_ONLY, example = "2024-11-23T13:59:21.382040700Z", description = "The time the error occurred.", requiredMode = Schema.RequiredMode.REQUIRED)
    private OffsetDateTime timestamp;

    @NotNull
    @Valid
    @Schema(name = "schemaValidationErrors", description = "Validation errors against the OpenAPI schema.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ValidationMessageDto> schemaValidationErrors = new ArrayList<>();
}
