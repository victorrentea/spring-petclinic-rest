package org.springframework.samples.petclinic.rest.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An user.
 */

@Schema(name = "User", description = "An user.")
@JsonTypeName("User")
public class UserDto {

  private String username;

  private @Nullable String password;

  private @Nullable Boolean enabled;

  @Valid
  private List<@Valid RoleDto> roles = new ArrayList<>();

    /**
   * The username
   * @return username
   */
  @NotNull @Size(min = 1, max = 80)
  @Schema(name = "username", example = "john.doe", description = "The username", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

    /**
   * The password
   * @return password
   */
  @Size(min = 1, max = 80)
  @Schema(name = "password", example = "1234abc", description = "The password", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public @Nullable String getPassword() {
    return password;
  }

  public void setPassword(@Nullable String password) {
    this.password = password;
  }

    /**
   * Indicates if the user is enabled
   * @return enabled
   */

  @Schema(name = "enabled", example = "true", description = "Indicates if the user is enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

    /**
   * The roles of an user
   * @return roles
   */
  @Valid
  @Schema(name = "roles", description = "The roles of an user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roles")
  public List<@Valid RoleDto> getRoles() {
    return roles;
  }

  public void setRoles(List<@Valid RoleDto> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto user = (UserDto) o;
    return Objects.equals(this.username, user.username) &&
        Objects.equals(this.password, user.password) &&
        Objects.equals(this.enabled, user.enabled) &&
        Objects.equals(this.roles, user.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, enabled, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDto {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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
