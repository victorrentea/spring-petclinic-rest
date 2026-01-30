package org.springframework.samples.petclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Models a {@link Vet Vet's} specialty (for example, dentistry).
 */
@Entity
@Table(name = "specialties")
public class Specialty extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
