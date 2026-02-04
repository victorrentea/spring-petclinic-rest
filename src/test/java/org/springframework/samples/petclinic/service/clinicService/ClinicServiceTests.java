package org.springframework.samples.petclinic.service.clinicService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
class ClinicServiceTests {

    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    VisitRepository visitRepository;
    @Autowired
    PetTypeRepository petTypeRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void shouldFindOwnersByLastName() {
        assertThat(ownerRepository.findByLastNameIgnoreCaseStartingWith("Davis").size()).isEqualTo(2);
        assertThat(ownerRepository.findByLastNameIgnoreCaseStartingWith("Daviss")).isEmpty();
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Owner owner = ownerRepository.findById(1).orElseThrow();
        assertThat(owner.getLastName()).startsWith("Franklin");
        assertThat(owner.getPets().size()).isEqualTo(1);
        assertThat(owner.getPets().get(0).getType()).isNotNull();
        assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
    }

    @Test
    void shouldInsertOwner() {
        List<Owner> owners = ownerRepository.findByLastNameIgnoreCaseStartingWith("Schultz");
        int found = owners.size();

        Owner owner = new Owner();
        owner.setFirstName("Sam");
        owner.setLastName("Schultz");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        ownerRepository.save(owner);
        assertThat(owner.getId().longValue()).isNotEqualTo(0);
        owners = ownerRepository.findByLastNameIgnoreCaseStartingWith("Schultz");
        assertThat(owners.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdateOwner() {
        Owner owner = ownerRepository.findById(1).orElseThrow();
        String oldLastName = owner.getLastName();
        String newLastName = oldLastName + "X";

        owner.setLastName(newLastName);
        ownerRepository.save(owner);

        // retrieving new name from database
        owner = ownerRepository.findById(1).orElseThrow();
        assertThat(owner.getLastName()).isEqualTo(newLastName);
    }

    @Test
    void shouldFindPetWithCorrectId() {
        Pet pet7 = petRepository.findById(7).orElseThrow();
        assertThat(pet7.getName()).startsWith("Samantha");
        assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");

    }

    @Test
    void shouldInsertPetIntoDatabaseAndGenerateId() {
        Owner owner6 = ownerRepository.findById(6).orElseThrow();
        int found = owner6.getPets().size();

        Pet pet = new Pet();
        pet.setName("bowser");
        List<PetType> types = petTypeRepository.findAll();
        pet.setType(EntityUtils.getById(types, PetType.class, 2));
        pet.setBirthDate(LocalDate.now());
        owner6.addPet(pet);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);

        petRepository.save(pet);
        ownerRepository.save(owner6);

        owner6 = ownerRepository.findById(6).orElseThrow();
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);
        // checks that id has been generated
        assertThat(pet.getId()).isNotNull();
    }

    @Test
    void shouldUpdatePetName() throws Exception {
        Pet pet7 = petRepository.findById(7).orElseThrow();
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        petRepository.save(pet7);

        pet7 = petRepository.findById(7).orElseThrow();
        assertThat(pet7.getName()).isEqualTo(newName);
    }


    @Test
    void shouldAddNewVisitForPet() {
        Pet pet7 = petRepository.findById(7).orElseThrow();
        int found = pet7.getVisits().size();
        Visit visit = new Visit();
        pet7.addVisit(visit);
        visit.setDescription("test");
        visitRepository.save(visit);
        petRepository.save(pet7);

        pet7 = petRepository.findById(7).orElseThrow();
        assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
        assertThat(visit.getId()).isNotNull();
    }

    @Test
    void shouldFindVisitsByPetId() throws Exception {
        List<Visit> visits = visitRepository.findByPetId(7);
        assertThat(visits.size()).isEqualTo(2);
        Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
        assertThat(visitArr[0].getPet()).isNotNull();
        assertThat(visitArr[0].getDate()).isNotNull();
        assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
    }

    @Test
    void shouldFindAllPets() {
        List<Pet> pets = petRepository.findAll();
        Pet pet1 = EntityUtils.getById(pets, Pet.class, 1);
        assertThat(pet1.getName()).isEqualTo("Leo");
        Pet pet3 = EntityUtils.getById(pets, Pet.class, 3);
        assertThat(pet3.getName()).isEqualTo("Rosy");
    }

    @Test
    void shouldDeletePet() {
        Pet pet = petRepository.findById(7).orElseThrow();
        petRepository.delete(pet);
        assertThatThrownBy(() -> petRepository.findById(7).orElseThrow())
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldFindVisitDyId() {
        Visit visit = visitRepository.findById(1).orElseThrow();
        assertThat(visit.getId()).isEqualTo(1);
        assertThat(visit.getPet().getName()).isEqualTo("Samantha");
    }

    @Test
    void shouldFindAllVisits() {
        List<Visit> visits = visitRepository.findAll();
        Visit visit1 = EntityUtils.getById(visits, Visit.class, 1);
        assertThat(visit1.getPet().getName()).isEqualTo("Samantha");
        Visit visit3 = EntityUtils.getById(visits, Visit.class, 3);
        assertThat(visit3.getPet().getName()).isEqualTo("Max");
    }

    @Test
    void shouldInsertVisit() {
        List<Visit> visits = visitRepository.findAll();
        int found = visits.size();

        Pet pet = petRepository.findById(1).orElseThrow();

        Visit visit = new Visit();
        visit.setPet(pet);
        visit.setDate(LocalDate.now());
        visit.setDescription("new visit");


        visitRepository.save(visit);
        assertThat(visit.getId().longValue()).isNotEqualTo(0);

        visits = visitRepository.findAll();
        assertThat(visits.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdateVisit() {
        Visit visit = visitRepository.findById(1).orElseThrow();
        String oldDesc = visit.getDescription();
        String newDesc = oldDesc + "X";
        visit.setDescription(newDesc);
        visitRepository.save(visit);
        visit = visitRepository.findById(1).orElseThrow();
        assertThat(visit.getDescription()).isEqualTo(newDesc);
    }

    @Test
    void shouldDeleteVisit() {
        Visit visit = visitRepository.findById(1).orElseThrow();
        visitRepository.delete(visit);
        try {
            visit = visitRepository.findById(1).orElseThrow();
        } catch (Exception e) {
            visit = null;
        }
        assertThat(visit).isNull();
    }


    @Test
    void shouldFindAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        Owner owner1 = EntityUtils.getById(owners, Owner.class, 1);
        assertThat(owner1.getFirstName()).isEqualTo("George");
        Owner owner3 = EntityUtils.getById(owners, Owner.class, 3);
        assertThat(owner3.getFirstName()).isEqualTo("Eduardo");
    }

    @Test
    void shouldDeleteOwner() {
        Owner owner = ownerRepository.findById(1).orElseThrow();
        ownerRepository.delete(owner);
        try {
            owner = ownerRepository.findById(1).orElseThrow();
        } catch (Exception e) {
            owner = null;
        }
        assertThat(owner).isNull();
    }

    @Test
    void shouldFindPetTypeById() {
        PetType petType = petTypeRepository.findById(1).orElseThrow();
        assertThat(petType.getName()).isEqualTo("cat");
    }

    @Test
    void shouldFindAllPetTypes() {
        List<PetType> petTypes = petTypeRepository.findAll();
        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType3 = EntityUtils.getById(petTypes, PetType.class, 3);
        assertThat(petType3.getName()).isEqualTo("lizard");
    }

    @Test
    void shouldInsertPetType() {
        List<PetType> petTypes = petTypeRepository.findAll();
        int found = petTypes.size();

        PetType petType = new PetType();
        petType.setName("tiger");

        petTypeRepository.save(petType);
        assertThat(petType.getId().longValue()).isNotEqualTo(0);

        petTypes = petTypeRepository.findAll();
        assertThat(petTypes.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdatePetType() {
        PetType petType = petTypeRepository.findById(1).orElseThrow();
        String oldLastName = petType.getName();
        String newLastName = oldLastName + "X";
        petType.setName(newLastName);
        petTypeRepository.save(petType);
        petType = petTypeRepository.findById(1).orElseThrow();
        assertThat(petType.getName()).isEqualTo(newLastName);
    }


    @Test
    void shouldFindSpecialtyById() {
        Specialty specialty = specialtyRepository.findById(1).orElseThrow();
        assertThat(specialty.getName()).isEqualTo("radiology");
    }

    @Test
    void shouldFindAllSpecialtys() {
        List<Specialty> specialties = specialtyRepository.findAll();
        Specialty specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
        assertThat(specialty1.getName()).isEqualTo("radiology");
        Specialty specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
        assertThat(specialty3.getName()).isEqualTo("dentistry");
    }

    @Test
    void shouldInsertSpecialty() {
        List<Specialty> specialties = specialtyRepository.findAll();
        int found = specialties.size();

        Specialty specialty = new Specialty();
        specialty.setName("dermatologist");

        specialtyRepository.save(specialty);
        assertThat(specialty.getId().longValue()).isNotEqualTo(0);

        specialties = specialtyRepository.findAll();
        assertThat(specialties.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdateSpecialty() {
        Specialty specialty = specialtyRepository.findById(1).orElseThrow();
        String oldLastName = specialty.getName();
        String newLastName = oldLastName + "X";
        specialty.setName(newLastName);
        specialtyRepository.save(specialty);
        specialty = specialtyRepository.findById(1).orElseThrow();
        assertThat(specialty.getName()).isEqualTo(newLastName);
    }

    @Test
    void shouldDeleteSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("test");
        specialtyRepository.save(specialty);
        Integer specialtyId = specialty.getId();
        assertThat(specialtyId).isNotNull();
        specialty = specialtyRepository.findById(specialtyId).orElseThrow();
        assertThat(specialty).isNotNull();
        specialtyRepository.delete(specialty);
        try {
            specialty = specialtyRepository.findById(specialtyId).orElseThrow();
        } catch (Exception e) {
            specialty = null;
        }
        assertThat(specialty).isNull();
    }
}
