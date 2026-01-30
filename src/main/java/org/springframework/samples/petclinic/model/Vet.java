package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vets")
public class Vet extends Person {

    @ManyToMany
    @JoinTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private List<Specialty> specialties;

    public Vet() {
        specialties = new ArrayList<>();
    }

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
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

}
