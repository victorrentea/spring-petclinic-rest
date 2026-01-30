package org.springframework.samples.petclinic.rest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "pet", description = "Endpoints related to pets.")
public class PetRestController {

    private final ClinicService clinicService;

    private final PetMapper petMapper;

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "/{petId}", produces = "application/json")
    public ResponseEntity<PetDto> getPet(@PathVariable Integer petId) {
        PetDto pet = petMapper.toPetDto(clinicService.findPetById(petId));
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<PetDto>> listPets() {
        List<PetDto> pets = new ArrayList<>(petMapper.toPetsDto(clinicService.findAllPets()));
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PutMapping(value = "/{petId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PetDto> updatePet(@PathVariable Integer petId, @Valid @RequestBody PetDto petDto) {
        Pet currentPet = clinicService.findPetById(petId);
        if (currentPet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPet.setBirthDate(petDto.getBirthDate());
        currentPet.setName(petDto.getName());
        currentPet.setType(petMapper.toPetType(petDto.getType()));
        clinicService.savePet(currentPet);
        return new ResponseEntity<>(petMapper.toPetDto(currentPet), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @DeleteMapping(value = "/{petId}", produces = "application/json")
    public ResponseEntity<PetDto> deletePet(@PathVariable Integer petId) {
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deletePet(pet);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
