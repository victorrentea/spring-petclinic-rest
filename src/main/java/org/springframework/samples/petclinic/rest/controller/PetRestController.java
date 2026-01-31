package org.springframework.samples.petclinic.rest.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
public class PetRestController {

    private final ClinicService clinicService;

    private final PetMapper petMapper;

    @GetMapping("/{petId}")
    public PetDto getPet(@PathVariable int petId) {
        return petMapper.toPetDto(clinicService.findPetById(petId));
    }

    @GetMapping(produces = "application/json")
    public List<PetDto> listPets() {
        List<Pet> allPets = clinicService.findAllPets();
        return petMapper.toPetsDto(allPets);
    }

    @PutMapping("/{petId}")
    @Transactional
    public void updatePet(@PathVariable int petId, @Validated @RequestBody PetDto petDto) {
        Pet currentPet = clinicService.findPetById(petId);
        currentPet
            .setBirthDate(petDto.getBirthDate())
            .setName(petDto.getName())
            .setType(petMapper.toPetType(petDto.getType()));
    }

    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable int petId) {
        Pet pet = clinicService.findPetById(petId);
        clinicService.deletePet(pet);
    }

}
