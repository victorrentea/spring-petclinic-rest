package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Visit;

import java.util.List;

public interface VisitRepository extends Repository<Visit, Integer> {

    Visit findById(int id);

    void save(Visit visit);

    List<Visit> findAll();

    void delete(Visit visit);

    List<Visit> findByPetId(int petId);

}
