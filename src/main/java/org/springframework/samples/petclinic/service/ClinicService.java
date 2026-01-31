package org.springframework.samples.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final PetRepository petRepository;
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
        return petRepository.findById(id).orElseThrow();
    }

    public void savePet(Pet pet) {
        pet.setType(findPetTypeById(pet.getType().getId()));
        petRepository.save(pet);
    }

    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
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

}
