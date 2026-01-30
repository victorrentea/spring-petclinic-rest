package org.springframework.samples.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 */
@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final OwnerRepository ownerRepository;
    private final VisitRepository visitRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PetTypeRepository petTypeRepository;

    @Override
    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }

    @Override
    @Transactional
    public void deletePet(Pet pet) {
        petRepository.delete(pet);
    }

    @Override
    public Visit findVisitById(int visitId) {
        return findEntityById(() -> visitRepository.findById(visitId));
    }

    @Override
    public List<Visit> findAllVisits() {
        return visitRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteVisit(Visit visit) {
        visitRepository.delete(visit);
    }

    @Override
    public Vet findVetById(int id) {
        return findEntityById(() -> vetRepository.findById(id));
    }

    @Override
    public List<Vet> findAllVets() {
        return vetRepository.findAll();
    }

    @Override
    public void saveVet(Vet vet) {
        vetRepository.save(vet);
    }

    @Override
    public void deleteVet(Vet vet) {
        vetRepository.delete(vet);
    }

    @Override
    public List<Owner> findAllOwners() {
        return ownerRepository.findAll();
    }

    @Override
    public void deleteOwner(Owner owner) {
        ownerRepository.delete(owner);
    }

    @Override
    public PetType findPetTypeById(int petTypeId) {
        return findEntityById(() -> petTypeRepository.findById(petTypeId));
    }

    @Override
    public List<PetType> findAllPetTypes() {
        return petTypeRepository.findAll();
    }

    @Override
    public void savePetType(PetType petType) {
        petTypeRepository.save(petType);
    }

    @Override
    public void deletePetType(PetType petType) {
        petTypeRepository.delete(petType);
    }

    @Override
    public Specialty findSpecialtyById(int specialtyId) {
        return findEntityById(() -> specialtyRepository.findById(specialtyId));
    }

    @Override
    public List<Specialty> findAllSpecialties() {
        return specialtyRepository.findAll();
    }

    @Override
    public void saveSpecialty(Specialty specialty) {
        specialtyRepository.save(specialty);
    }

    @Override
    public void deleteSpecialty(Specialty specialty) {
        specialtyRepository.delete(specialty);
    }

    @Override
    public List<PetType> findPetTypes() {
        return petRepository.findPetTypes();
    }

    @Override
    public Owner findOwnerById(int id) {
        return findEntityById(() -> ownerRepository.findById(id));
    }

    @Override
    public Pet findPetById(int id) {
        return findEntityById(() -> petRepository.findById(id));
    }

    @Override
    public void savePet(Pet pet) {
        pet.setType(findPetTypeById(pet.getType().getId()));
        petRepository.save(pet);
    }

    @Override
    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
    }

    @Override
    public List<Vet> findVets() {
        return vetRepository.findAll();
    }

    @Override
    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }

    @Override
    public List<Owner> findOwnerByLastName(String lastName) {
        return ownerRepository.findByLastNameIgnoreCaseStartingWith(lastName);
    }

    @Override
    public List<Visit> findVisitsByPetId(int petId) {
        return visitRepository.findByPetId(petId);
    }

    @Override
    public List<Specialty> findSpecialtiesByNameIn(Set<String> names) {
        return findEntityById(() -> specialtyRepository.findSpecialtiesByNameIn(names));
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
