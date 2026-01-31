package org.springframework.samples.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final OwnerRepository ownerRepository;
    private final VisitRepository visitRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PetTypeRepository petTypeRepository;

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    public void deletePet(Pet pet) {
        petRepository.delete(pet);
    }

    public Visit findVisitById(int visitId) {
        return visitRepository.findById(visitId).orElseThrow();
    }

    public List<Visit> findAllVisits() {
        return visitRepository.findAll();
    }

    public void deleteVisit(Visit visit) {
        visitRepository.delete(visit);
    }

    public Vet findVetById(int id) {
        return vetRepository.findById(id).orElseThrow();
    }

    public List<Vet> findAllVets() {
        return vetRepository.findAll();
    }

    public void saveVet(Vet vet) {
        vetRepository.save(vet);
    }

    public void deleteVet(Vet vet) {
        vetRepository.delete(vet);
    }

    public List<Owner> findAllOwners() {
        return ownerRepository.findAll();
    }

    public void deleteOwner(Owner owner) {
        ownerRepository.delete(owner);
    }

    public PetType findPetTypeById(int petTypeId) {
        return petTypeRepository.findById(petTypeId).orElseThrow();
    }

    public List<PetType> findAllPetTypes() {
        return petTypeRepository.findAll();
    }

    public void savePetType(PetType petType) {
        petTypeRepository.save(petType);
    }

    public void deletePetType(PetType petType) {
        petTypeRepository.delete(petType);
    }

    public Specialty findSpecialtyById(int specialtyId) {
        return specialtyRepository.findById(specialtyId).orElseThrow();
    }

    public List<Specialty> findAllSpecialties() {
        return specialtyRepository.findAll();
    }

    public void saveSpecialty(Specialty specialty) {
        specialtyRepository.save(specialty);
    }

    public void deleteSpecialty(Specialty specialty) {
        specialtyRepository.delete(specialty);
    }

    public List<PetType> findPetTypes() {
        return petRepository.findPetTypes();
    }

    public Owner findOwnerById(int id) {
        return ownerRepository.findById(id).orElseThrow();
    }

    public Pet findPetById(int id) {
        Optional<Pet> po = petRepository.findById(id);
        return po.orElseThrow();
    }

    public void savePet(Pet pet) {
        pet.setType(findPetTypeById(pet.getType().getId()));
        petRepository.save(pet);
    }

    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
    }

    public List<Vet> findVets() {
        return vetRepository.findAll();
    }

    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }

    public List<Owner> findOwnerByLastName(String lastName) {
        return ownerRepository.findByLastNameIgnoreCaseStartingWith(lastName);
    }

    public List<Visit> findVisitsByPetId(int petId) {
        return visitRepository.findByPetId(petId);
    }

    public List<Specialty> findSpecialtiesByNameIn(Set<String> names) {
        return specialtyRepository.findSpecialtiesByNameIn(names);
    }

    private <T> T findEntityById(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            // Just ignore not found exceptions for Jdbc/Jpa realization
            return null;
        }
    }

}
