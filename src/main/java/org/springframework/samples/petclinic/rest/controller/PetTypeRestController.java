package org.springframework.samples.petclinic.rest.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pettypes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
public class PetTypeRestController {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;

    @GetMapping(produces = "application/json")
    public List<PetTypeDto> listPetTypes() {
        List<PetType> petTypes = new ArrayList<>(clinicService.findAllPetTypes());
        return petTypeMapper.toPetTypeDtos(petTypes);
    }

    @GetMapping("/{petTypeId}")
    public PetTypeDto getPetType(@PathVariable int petTypeId) {
        PetType petType = clinicService.findPetTypeById(petTypeId);
        return petTypeMapper.toPetTypeDto(petType);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> addPetType(@RequestBody @Validated PetTypeFieldsDto petTypeFieldsDto) {
        PetType type = petTypeMapper.toPetType(petTypeFieldsDto);
        clinicService.savePetType(type);
        URI createdUri = UriComponentsBuilder.fromPath("/api/pettypes/{id}")
            .buildAndExpand(type.getId()).toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PutMapping("/{petTypeId}")
    public void updatePetType(@PathVariable int petTypeId,
                              @RequestBody @Validated PetTypeDto petTypeDto) {
        PetType currentPetType = clinicService.findPetTypeById(petTypeId);
        currentPetType.setName(petTypeDto.getName());
        clinicService.savePetType(currentPetType);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    @DeleteMapping("/{petTypeId}")
    public void deletePetType(@PathVariable int petTypeId) {
        PetType petType = clinicService.findPetTypeById(petTypeId);
        clinicService.deletePetType(petType);
    }

}
