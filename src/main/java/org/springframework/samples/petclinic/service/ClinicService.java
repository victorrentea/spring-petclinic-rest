package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Mostly used as a facade so all controllers have a single point of entry
 */
public interface ClinicService {

	Pet findPetById(int id) ;
	List<Pet> findAllPets() ;
	void savePet(Pet pet) ;
	void deletePet(Pet pet) ;

	List<Visit> findVisitsByPetId(int petId);
	Visit findVisitById(int visitId) ;
	List<Visit> findAllVisits() ;
	void saveVisit(Visit visit) ;
	void deleteVisit(Visit visit) ;
	Vet findVetById(int id) ;
	List<Vet> findVets() ;
	List<Vet> findAllVets() ;
	void saveVet(Vet vet) ;
	void deleteVet(Vet vet) ;
	Owner findOwnerById(int id) ;
	List<Owner> findAllOwners() ;
	void saveOwner(Owner owner) ;
	void deleteOwner(Owner owner) ;
	List<Owner> findOwnerByLastName(String lastName) ;

	PetType findPetTypeById(int petTypeId);
	List<PetType> findAllPetTypes() ;
	List<PetType> findPetTypes() ;
	void savePetType(PetType petType) ;
	void deletePetType(PetType petType) ;
	Specialty findSpecialtyById(int specialtyId);
	List<Specialty> findAllSpecialties() ;
	void saveSpecialty(Specialty specialty) ;
	void deleteSpecialty(Specialty specialty) ;

    List<Specialty> findSpecialtiesByNameIn(Set<String> names) ;
}
