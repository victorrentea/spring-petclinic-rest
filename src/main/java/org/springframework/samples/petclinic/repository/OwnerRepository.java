package org.springframework.samples.petclinic.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Owner;

import java.util.List;

public interface OwnerRepository extends Repository<Owner, Integer> {

    List<Owner> findByLastNameIgnoreCaseStartingWith(String lastName);

    Owner findById(int id);

    void save(Owner owner);

    List<Owner> findAll();

    void delete(Owner owner);

}
