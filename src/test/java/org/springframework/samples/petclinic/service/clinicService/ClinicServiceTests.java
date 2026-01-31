package org.springframework.samples.petclinic.service.clinicService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.service.ClinicService;
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
    ClinicService clinicService;
    @Autowired
    EntityManager entityManager;

    @Test
    void shouldFindOwnersByLastName() {
        assertThat(clinicService.findOwnerByLastName("Davis").size()).isEqualTo(2);
        assertThat(clinicService.findOwnerByLastName("Daviss")).isEmpty();
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Owner owner = clinicService.findOwnerById(1);
        assertThat(owner.getLastName()).startsWith("Franklin");
        assertThat(owner.getPets().size()).isEqualTo(1);
        assertThat(owner.getPets().get(0).getType()).isNotNull();
        assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
    }

    @Test
    void shouldInsertOwner() {
        List<Owner> owners = clinicService.findOwnerByLastName("Schultz");
        int found = owners.size();

        Owner owner = new Owner();
        owner.setFirstName("Sam");
        owner.setLastName("Schultz");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        clinicService.saveOwner(owner);
        assertThat(owner.getId().longValue()).isNotEqualTo(0);
        owners = clinicService.findOwnerByLastName("Schultz");
        assertThat(owners.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdateOwner() {
        Owner owner = clinicService.findOwnerById(1);
        String oldLastName = owner.getLastName();
        String newLastName = oldLastName + "X";

        owner.setLastName(newLastName);
        clinicService.saveOwner(owner);

        // retrieving new name from database
        owner = clinicService.findOwnerById(1);
        assertThat(owner.getLastName()).isEqualTo(newLastName);
    }

    @Test
    void shouldFindPetWithCorrectId() {
        Pet pet7 = clinicService.findPetById(7);
        assertThat(pet7.getName()).startsWith("Samantha");
        assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");

    }

//    @Test
//    void shouldFindAllPetTypes() {
//        List<PetType> petTypes = this.clinicService.findPetTypes();
//
//        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
//        assertThat(petType1.getName()).isEqualTo("cat");
//        PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
//        assertThat(petType4.getName()).isEqualTo("snake");
//    }

    @Test
    void shouldInsertPetIntoDatabaseAndGenerateId() {
        Owner owner6 = clinicService.findOwnerById(6);
        int found = owner6.getPets().size();

        Pet pet = new Pet();
        pet.setName("bowser");
        List<PetType> types = clinicService.findPetTypes();
        pet.setType(EntityUtils.getById(types, PetType.class, 2));
        pet.setBirthDate(LocalDate.now());
        owner6.addPet(pet);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);

        clinicService.savePet(pet);
        clinicService.saveOwner(owner6);

        owner6 = clinicService.findOwnerById(6);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);
        // checks that id has been generated
        assertThat(pet.getId()).isNotNull();
    }

    @Test
    void shouldUpdatePetName() throws Exception {
        Pet pet7 = clinicService.findPetById(7);
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        clinicService.savePet(pet7);

        pet7 = clinicService.findPetById(7);
        assertThat(pet7.getName()).isEqualTo(newName);
    }


    @Test
    void shouldAddNewVisitForPet() {
        Pet pet7 = clinicService.findPetById(7);
        int found = pet7.getVisits().size();
        Visit visit = new Visit();
        pet7.addVisit(visit);
        visit.setDescription("test");
        clinicService.saveVisit(visit);
        clinicService.savePet(pet7);

        pet7 = clinicService.findPetById(7);
        assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
        assertThat(visit.getId()).isNotNull();
    }

    @Test
    void shouldFindVisitsByPetId() throws Exception {
        List<Visit> visits = clinicService.findVisitsByPetId(7);
        assertThat(visits.size()).isEqualTo(2);
        Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
        assertThat(visitArr[0].getPet()).isNotNull();
        assertThat(visitArr[0].getDate()).isNotNull();
        assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
    }

    @Test
    void shouldFindAllPets() {
        List<Pet> pets = clinicService.findAllPets();
        Pet pet1 = EntityUtils.getById(pets, Pet.class, 1);
        assertThat(pet1.getName()).isEqualTo("Leo");
        Pet pet3 = EntityUtils.getById(pets, Pet.class, 3);
        assertThat(pet3.getName()).isEqualTo("Rosy");
    }

    @Test
    void shouldDeletePet() {
        Pet pet = clinicService.findPetById(7);
        clinicService.deletePet(pet);
        assertThatThrownBy(() -> clinicService.findPetById(7))
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldFindVisitDyId() {
        Visit visit = clinicService.findVisitById(1);
        assertThat(visit.getId()).isEqualTo(1);
        assertThat(visit.getPet().getName()).isEqualTo("Samantha");
    }

    @Test
    void shouldFindAllVisits() {
        List<Visit> visits = clinicService.findAllVisits();
        Visit visit1 = EntityUtils.getById(visits, Visit.class, 1);
        assertThat(visit1.getPet().getName()).isEqualTo("Samantha");
        Visit visit3 = EntityUtils.getById(visits, Visit.class, 3);
        assertThat(visit3.getPet().getName()).isEqualTo("Max");
    }

    @Test
    void shouldInsertVisit() {
        List<Visit> visits = clinicService.findAllVisits();
        int found = visits.size();

        Pet pet = clinicService.findPetById(1);

        Visit visit = new Visit();
        visit.setPet(pet);
        visit.setDate(LocalDate.now());
        visit.setDescription("new visit");


        clinicService.saveVisit(visit);
        assertThat(visit.getId().longValue()).isNotEqualTo(0);

        visits = clinicService.findAllVisits();
        assertThat(visits.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdateVisit() {
        Visit visit = clinicService.findVisitById(1);
        String oldDesc = visit.getDescription();
        String newDesc = oldDesc + "X";
        visit.setDescription(newDesc);
        clinicService.saveVisit(visit);
        visit = clinicService.findVisitById(1);
        assertThat(visit.getDescription()).isEqualTo(newDesc);
    }

    @Test
    void shouldDeleteVisit() {
        Visit visit = clinicService.findVisitById(1);
        clinicService.deleteVisit(visit);
        try {
            visit = clinicService.findVisitById(1);
        } catch (Exception e) {
            visit = null;
        }
        assertThat(visit).isNull();
    }


    @Test
    void shouldFindAllOwners() {
        List<Owner> owners = clinicService.findAllOwners();
        Owner owner1 = EntityUtils.getById(owners, Owner.class, 1);
        assertThat(owner1.getFirstName()).isEqualTo("George");
        Owner owner3 = EntityUtils.getById(owners, Owner.class, 3);
        assertThat(owner3.getFirstName()).isEqualTo("Eduardo");
    }

    @Test
    void shouldDeleteOwner() {
        Owner owner = clinicService.findOwnerById(1);
        clinicService.deleteOwner(owner);
        try {
            owner = clinicService.findOwnerById(1);
        } catch (Exception e) {
            owner = null;
        }
        assertThat(owner).isNull();
    }

    @Test
    void shouldFindPetTypeById() {
        PetType petType = clinicService.findPetTypeById(1);
        assertThat(petType.getName()).isEqualTo("cat");
    }

    @Test
    void shouldFindAllPetTypes() {
        List<PetType> petTypes = clinicService.findAllPetTypes();
        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType3 = EntityUtils.getById(petTypes, PetType.class, 3);
        assertThat(petType3.getName()).isEqualTo("lizard");
    }

    @Test
    void shouldInsertPetType() {
        List<PetType> petTypes = clinicService.findAllPetTypes();
        int found = petTypes.size();

        PetType petType = new PetType();
        petType.setName("tiger");

        clinicService.savePetType(petType);
        assertThat(petType.getId().longValue()).isNotEqualTo(0);

        petTypes = clinicService.findAllPetTypes();
        assertThat(petTypes.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdatePetType() {
        PetType petType = clinicService.findPetTypeById(1);
        String oldLastName = petType.getName();
        String newLastName = oldLastName + "X";
        petType.setName(newLastName);
        clinicService.savePetType(petType);
        petType = clinicService.findPetTypeById(1);
        assertThat(petType.getName()).isEqualTo(newLastName);
    }


    @Test
    void shouldFindSpecialtyById() {
        Specialty specialty = clinicService.findSpecialtyById(1);
        assertThat(specialty.getName()).isEqualTo("radiology");
    }

    @Test
    void shouldFindAllSpecialtys() {
        List<Specialty> specialties = clinicService.findAllSpecialties();
        Specialty specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
        assertThat(specialty1.getName()).isEqualTo("radiology");
        Specialty specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
        assertThat(specialty3.getName()).isEqualTo("dentistry");
    }

    @Test
    void shouldInsertSpecialty() {
        List<Specialty> specialties = clinicService.findAllSpecialties();
        int found = specialties.size();

        Specialty specialty = new Specialty();
        specialty.setName("dermatologist");

        clinicService.saveSpecialty(specialty);
        assertThat(specialty.getId().longValue()).isNotEqualTo(0);

        specialties = clinicService.findAllSpecialties();
        assertThat(specialties.size()).isEqualTo(found + 1);
    }

    @Test
    void shouldUpdateSpecialty() {
        Specialty specialty = clinicService.findSpecialtyById(1);
        String oldLastName = specialty.getName();
        String newLastName = oldLastName + "X";
        specialty.setName(newLastName);
        clinicService.saveSpecialty(specialty);
        specialty = clinicService.findSpecialtyById(1);
        assertThat(specialty.getName()).isEqualTo(newLastName);
    }

    @Test
    void shouldDeleteSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("test");
        clinicService.saveSpecialty(specialty);
        Integer specialtyId = specialty.getId();
        assertThat(specialtyId).isNotNull();
        specialty = clinicService.findSpecialtyById(specialtyId);
        assertThat(specialty).isNotNull();
        clinicService.deleteSpecialty(specialty);
        try {
            specialty = clinicService.findSpecialtyById(specialtyId);
        } catch (Exception e) {
            specialty = null;
        }
        assertThat(specialty).isNull();
    }
}
