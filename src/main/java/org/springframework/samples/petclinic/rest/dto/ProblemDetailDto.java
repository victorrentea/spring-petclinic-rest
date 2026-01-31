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

@Schema(description = "The schema for all error responses.")
@Data
public class ProblemDetailDto {

    @Valid
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "http://localhost:9966/petclinic/api/owner", description = "Full URL that originated the error response.")
    private URI type;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "NoResourceFoundException", description = "The short error title.")
    private String title;

    @Min(400)
    @Max(600)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "500", description = "HTTP status code")
    private Integer status;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "No static resource api/owner.", description = "The long error message.")
    private String detail;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Valid
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "2024-11-23T13:59:21.382040700Z", description = "The time the error occurred.")
    private OffsetDateTime timestamp;

    @NotNull
    @Valid
    @Schema(description = "Validation errors against the OpenAPI schema.")
    private List<ValidationMessageDto> schemaValidationErrors = new ArrayList<>();
}
