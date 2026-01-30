package org.springframework.samples.petclinic.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Tag(name = "owner", description = "Endpoints related to owners.")
public class OwnerRestController {

    private final ClinicService clinicService;

    private final OwnerMapper ownerMapper;

    private final PetMapper petMapper;

    private final VisitMapper visitMapper;

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "listOwners", summary = "List owners")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<OwnerDto>> listOwners(@RequestParam(name = "lastName", required = false) String lastName) {
        List<Owner> owners;
        if (lastName != null) {
            owners = clinicService.findOwnerByLastName(lastName);
        } else {
            owners = clinicService.findAllOwners();
        }
        if (owners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ownerMapper.toOwnerDtoCollection(owners), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "getOwner", summary = "Get an owner by ID")
    @GetMapping(value = "/{ownerId}")
    public ResponseEntity<OwnerDto> getOwner(@PathVariable Integer ownerId) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ownerMapper.toOwnerDto(owner), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "addOwner", summary = "Create an owner")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<OwnerDto> addOwner(@RequestBody OwnerFieldsDto ownerFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        Owner owner = ownerMapper.toOwner(ownerFieldsDto);
        clinicService.saveOwner(owner);
        OwnerDto ownerDto = ownerMapper.toOwnerDto(owner);
        headers.setLocation(UriComponentsBuilder.newInstance()
            .path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
        return new ResponseEntity<>(ownerDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "updateOwner", summary = "Update an owner")
    @PutMapping(value = "/{ownerId}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable Integer ownerId, @RequestBody OwnerFieldsDto ownerFieldsDto) {
        Owner currentOwner = clinicService.findOwnerById(ownerId);
        if (currentOwner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentOwner.setAddress(ownerFieldsDto.getAddress());
        currentOwner.setCity(ownerFieldsDto.getCity());
        currentOwner.setFirstName(ownerFieldsDto.getFirstName());
        currentOwner.setLastName(ownerFieldsDto.getLastName());
        currentOwner.setTelephone(ownerFieldsDto.getTelephone());
        clinicService.saveOwner(currentOwner);
        return new ResponseEntity<>(ownerMapper.toOwnerDto(currentOwner), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "deleteOwner", summary = "Delete an owner by ID")
    @DeleteMapping(value = "/{ownerId}")
    public ResponseEntity<OwnerDto> deleteOwner(@PathVariable Integer ownerId) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deleteOwner(owner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "addPetToOwner", summary = "Add a pet to an owner")
    @PostMapping(value = "{ownerId}/pets")
    public ResponseEntity<PetDto> addPetToOwner(@PathVariable Integer ownerId, @RequestBody PetFieldsDto petFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        Pet pet = petMapper.toPet(petFieldsDto);
        Owner owner = new Owner();
        owner.setId(ownerId);
        pet.setOwner(owner);
        pet.getType().setName(null);
        clinicService.savePet(pet);
        PetDto petDto = petMapper.toPetDto(pet);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pets/{id}")
            .buildAndExpand(pet.getId()).toUri());
        return new ResponseEntity<>(petDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "updateOwnersPet", summary = "Update an owner's pet")
    @PutMapping(value = "{ownerId}/pets/{petId}")
    public ResponseEntity<Void> updateOwnersPet(@PathVariable Integer ownerId, @PathVariable Integer petId, @RequestBody PetFieldsDto petFieldsDto) {
        Owner currentOwner = clinicService.findOwnerById(ownerId);
        if (currentOwner != null) {
            Pet currentPet = clinicService.findPetById(petId);
            if (currentPet != null) {
                currentPet.setBirthDate(petFieldsDto.getBirthDate());
                currentPet.setName(petFieldsDto.getName());
                currentPet.setType(petMapper.toPetType(petFieldsDto.getType()));
                clinicService.savePet(currentPet);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "addVisitToOwner", summary = "Add a visit for an owner's pet")
    @PostMapping(value = "{ownerId}/pets/{petId}/visits")
    public ResponseEntity<VisitDto> addVisitToOwner(@PathVariable Integer ownerId, @PathVariable Integer petId, @RequestBody VisitFieldsDto visitFieldsDto) {
        Visit visit = visitMapper.toVisit(visitFieldsDto);
        Pet pet = new Pet();
        pet.setId(petId);
        visit.setPet(pet);
        clinicService.saveVisit(visit);
        VisitDto visitDto = visitMapper.toVisitDto(visit);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/visits/{id}")
            .buildAndExpand(visit.getId()).toUri());
        return new ResponseEntity<>(visitDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Operation(operationId = "getOwnersPet", summary = "Get a pet belonging to an owner")
    @GetMapping(value = "{ownerId}/pets/{petId}")
    public ResponseEntity<PetDto> getOwnersPet(@PathVariable Integer ownerId, @PathVariable Integer petId) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (owner != null) {
            Pet pet = owner.getPet(petId);
            if (pet != null) {
                return new ResponseEntity<>(petMapper.toPetDto(pet), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
