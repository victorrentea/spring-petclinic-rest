package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Getter
@Setter
public class Visit extends BaseEntity {
    @Column(name = "visit_date", columnDefinition = "DATE")
    private LocalDate date = LocalDate.now();

    @NotEmpty
    private String description;

    @ManyToOne
    private Pet pet;

}
