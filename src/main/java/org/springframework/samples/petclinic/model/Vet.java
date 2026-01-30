package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vets")
@Getter
@Setter
public class Vet {

    @NotEmpty
    protected String firstName;
    @NotEmpty
    protected String lastName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @ManyToMany
    @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private List<Specialty> specialties;

    public Vet() {
        specialties = new ArrayList<>();
    }

    public void clearSpecialties() {
        this.specialties.clear();
    }

    public int getNrOfSpecialties() {
        return specialties.size();
    }

    public void addSpecialty(Specialty specialty) {
        specialties.add(specialty);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Integer getId() {
        return this.id;
    }

    public Vet setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Vet setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Vet setId(Integer id) {
        this.id = id;
        return this;
    }
}
