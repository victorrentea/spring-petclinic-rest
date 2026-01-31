package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

@Schema(name = "ValidationMessage", description = "Messages describing a validation error.")
@JsonTypeName("ValidationMessage")
@Data
public class ValidationMessageDto {

    @Schema(name = "message", accessMode = Schema.AccessMode.READ_ONLY, example = "[Path '/lastName'] Instance type (null) does not match any allowed primitive type (allowed: ['string'])", description = "The validation message.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    private Map<String, Object> additionalProperties;

    @JsonAnySetter
    public ValidationMessageDto putAdditionalProperty(String key, Object value) {
        if (this.additionalProperties == null) {
            this.additionalProperties = new HashMap<String, Object>();
        }
        this.additionalProperties.put(key, value);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
}
