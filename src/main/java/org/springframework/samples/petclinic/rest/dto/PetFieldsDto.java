package org.springframework.samples.petclinic.rest.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Editable fields of a pet.
 */

@Schema(name = "PetFields", description = "Editable fields of a pet.")
public class PetFieldsDto {

  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthDate;

  private PetTypeDto type;

    /**
   * The name of the pet.
   * @return name
   */
  @NotNull @Size(max = 30)
  @Schema(name = "name", example = "Leo", description = "The name of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

    /**
   * The date of birth of the pet.
   * @return birthDate
   */
  @NotNull @Valid
  @Schema(name = "birthDate", example = "2010-09-07", description = "The date of birth of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("birthDate")
  public LocalDate getBirthDate() {
    return birthDate;
  }

    /**
   * Get type
   * @return type
   */
  @NotNull @Valid
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public PetTypeDto getType() {
    return type;
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PetFieldsDto petFields = (PetFieldsDto) o;
    return Objects.equals(this.name, petFields.name) &&
        Objects.equals(this.birthDate, petFields.birthDate) &&
        Objects.equals(this.type, petFields.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, birthDate, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PetFieldsDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
