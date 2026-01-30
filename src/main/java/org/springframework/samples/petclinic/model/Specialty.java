package org.springframework.samples.petclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Models a {@link Vet Vet's} specialty (for example, dentistry).
 */
@Entity
@Table(name = "specialties")
@Getter
@Setter
public class Specialty extends BaseEntity {

    private String name;

}
