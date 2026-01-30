package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Vet;

import java.util.Collection;
import java.util.List;

public interface VetRepository extends Repository<Vet, Integer> {
    List<Vet> findAll();

	Vet findById(int id);

	void save(Vet vet);

	void delete(Vet vet);

}
