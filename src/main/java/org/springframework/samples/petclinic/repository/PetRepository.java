package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;

import java.util.List;

public interface PetRepository extends Repository<Pet, Integer> {

    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    List<PetType> findPetTypes();

    Pet findById(int id);

    void save(Pet pet);

    List<Pet> findAll();

    void delete(Pet pet);

}
