package org.springframework.samples.petclinic.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@Getter
@Setter
public class NamedEntity extends BaseEntity {

    @NotEmpty
    private String name;

    @Override
    public String toString() {
        return getName();
    }

}
