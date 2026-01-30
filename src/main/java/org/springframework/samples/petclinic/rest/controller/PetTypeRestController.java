package org.springframework.samples.petclinic.rest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pettypes")
@RequiredArgsConstructor
@Tag(name = "pettypes", description = "Endpoints related to pet types.")
public class PetTypeRestController {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;


    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<PetTypeDto>> listPetTypes() {
        List<PetType> petTypes = new ArrayList<>(clinicService.findAllPetTypes());
        if (petTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDtos(petTypes), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    @GetMapping(value = "/{petTypeId}")
    public ResponseEntity<PetTypeDto> getPetType(@PathVariable("petTypeId") Integer petTypeId) {
        PetType petType = clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<PetTypeDto> addPetType(@RequestBody PetTypeFieldsDto petTypeFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        PetType type = petTypeMapper.toPetType(petTypeFieldsDto);
        clinicService.savePetType(type);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pettypes/{id}").buildAndExpand(type.getId()).toUri());
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PutMapping(value = "/{petTypeId}")
    public ResponseEntity<PetTypeDto> updatePetType(@PathVariable("petTypeId") Integer petTypeId, @RequestBody PetTypeDto petTypeDto) {
        PetType currentPetType = clinicService.findPetTypeById(petTypeId);
        if (currentPetType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPetType.setName(petTypeDto.getName());
        clinicService.savePetType(currentPetType);
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(currentPetType), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    @DeleteMapping(value = "/{petTypeId}")
    public ResponseEntity<PetTypeDto> deletePetType(@PathVariable("petTypeId") Integer petTypeId) {
        PetType petType = clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deletePetType(petType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
