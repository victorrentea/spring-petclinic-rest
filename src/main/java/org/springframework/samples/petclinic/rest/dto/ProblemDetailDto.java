package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The schema for all error responses.
 */

@Schema(name = "ProblemDetail", description = "The schema for all error responses.")
@JsonTypeName("ProblemDetail")

public class ProblemDetailDto {

  private URI type;

  private String title;

  private Integer status;

  private String detail;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  @Valid
  private List<ValidationMessageDto> schemaValidationErrors = new ArrayList<>();

    /**
   * Full URL that originated the error response.
   * @return type
   */
  @Valid
  @Schema(name = "type", accessMode = Schema.AccessMode.READ_ONLY, example = "http://localhost:9966/petclinic/api/owner", description = "Full URL that originated the error response.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public URI getType() {
    return type;
  }

    /**
   * The short error title.
   * @return title
   */

  @Schema(name = "title", accessMode = Schema.AccessMode.READ_ONLY, example = "NoResourceFoundException", description = "The short error title.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

    /**
   * HTTP status code
   * minimum: 400
   * maximum: 600
   * @return status
   */
  @Min(400)
  @Max(600)
  @Schema(name = "status", accessMode = Schema.AccessMode.READ_ONLY, example = "500", description = "HTTP status code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

    /**
   * The long error message.
   * @return detail
   */

  @Schema(name = "detail", accessMode = Schema.AccessMode.READ_ONLY, example = "No static resource api/owner.", description = "The long error message.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("detail")
  public String getDetail() {
    return detail;
  }

    /**
   * The time the error occurred.
   * @return timestamp
   */
  @Valid
  @Schema(name = "timestamp", accessMode = Schema.AccessMode.READ_ONLY, example = "2024-11-23T13:59:21.382040700Z", description = "The time the error occurred.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

    /**
   * Validation errors against the OpenAPI schema.
   * @return schemaValidationErrors
   */
  @NotNull @Valid
  @Schema(name = "schemaValidationErrors", description = "Validation errors against the OpenAPI schema.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("schemaValidationErrors")
  public List<ValidationMessageDto> getSchemaValidationErrors() {
    return schemaValidationErrors;
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProblemDetailDto problemDetail = (ProblemDetailDto) o;
    return Objects.equals(this.type, problemDetail.type) &&
        Objects.equals(this.title, problemDetail.title) &&
        Objects.equals(this.status, problemDetail.status) &&
        Objects.equals(this.detail, problemDetail.detail) &&
        Objects.equals(this.timestamp, problemDetail.timestamp) &&
        Objects.equals(this.schemaValidationErrors, problemDetail.schemaValidationErrors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, title, status, detail, timestamp, schemaValidationErrors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProblemDetailDto {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    schemaValidationErrors: ").append(toIndentedString(schemaValidationErrors)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
