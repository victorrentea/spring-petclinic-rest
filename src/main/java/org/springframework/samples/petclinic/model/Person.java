package org.springframework.samples.petclinic.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Person extends BaseEntity {

    @NotEmpty
    protected String firstName;

    @NotEmpty
    protected String lastName;

}
