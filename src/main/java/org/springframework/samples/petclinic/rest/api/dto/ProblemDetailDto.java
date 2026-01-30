package org.springframework.samples.petclinic.rest.api.dto;

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


import jakarta.annotation.Generated;

/**
 * The schema for all error responses.
 */

@Schema(name = "ProblemDetail", description = "The schema for all error responses.")
@JsonTypeName("ProblemDetail")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-30T03:06:39.394949+02:00[Europe/Bucharest]", comments = "Generator version: 7.18.0")
public class ProblemDetailDto {

  private URI type;

  private String title;

  private Integer status;

  private String detail;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  @Valid
  private List<ValidationMessageDto> schemaValidationErrors = new ArrayList<>();

  public ProblemDetailDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProblemDetailDto(URI type, String title, Integer status, String detail, OffsetDateTime timestamp, List<ValidationMessageDto> schemaValidationErrors) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.timestamp = timestamp;
    this.schemaValidationErrors = schemaValidationErrors;
  }

  public ProblemDetailDto type(URI type) {
    this.type = type;
    return this;
  }

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

  public void setType(URI type) {
    this.type = type;
  }

  public ProblemDetailDto title(String title) {
    this.title = title;
    return this;
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

  public void setTitle(String title) {
    this.title = title;
  }

  public ProblemDetailDto status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * HTTP status code
   * minimum: 400
   * maximum: 600
   * @return status
   */
  @Min(value = 400) @Max(value = 600)
  @Schema(name = "status", accessMode = Schema.AccessMode.READ_ONLY, example = "500", description = "HTTP status code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ProblemDetailDto detail(String detail) {
    this.detail = detail;
    return this;
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

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public ProblemDetailDto timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
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

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ProblemDetailDto schemaValidationErrors(List<ValidationMessageDto> schemaValidationErrors) {
    this.schemaValidationErrors = schemaValidationErrors;
    return this;
  }

  public ProblemDetailDto addSchemaValidationErrorsItem(ValidationMessageDto schemaValidationErrorsItem) {
    if (this.schemaValidationErrors == null) {
      this.schemaValidationErrors = new ArrayList<>();
    }
    this.schemaValidationErrors.add(schemaValidationErrorsItem);
    return this;
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

  public void setSchemaValidationErrors(List<ValidationMessageDto> schemaValidationErrors) {
    this.schemaValidationErrors = schemaValidationErrors;
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

