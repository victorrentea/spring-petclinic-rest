package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Specialty;

import java.util.List;
import java.util.Set;

public interface SpecialtyRepository extends Repository<Specialty, Integer> {

    Specialty findById(int id);

    List<Specialty> findSpecialtiesByNameIn(Set<String> names);

    List<Specialty> findAll();

    void save(Specialty specialty);

    void delete(Specialty specialty);

}
