package org.springframework.samples.petclinic.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole(@roles.VET_ADMIN)")
public class SpecialtyRestController {
    private final ClinicService clinicService;
    private final SpecialtyMapper specialtyMapper;

    @GetMapping("/specialties")
    public List<SpecialtyDto> listSpecialties() {
        List<Specialty> allSpecialties = clinicService.findAllSpecialties();
        return specialtyMapper.toSpecialtyDtos(allSpecialties);
    }

    @GetMapping("/specialties/{specialtyId}")
    public SpecialtyDto getSpecialty(@PathVariable int specialtyId) {
        Specialty specialty = clinicService.findSpecialtyById(specialtyId);
        return specialtyMapper.toSpecialtyDto(specialty);
    }

    @PostMapping("/specialties")
    public ResponseEntity<Void> addSpecialty(@RequestBody @Validated SpecialtyDto specialtyDto) {
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        clinicService.saveSpecialty(specialty);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/specialties/{id}")
                        .buildAndExpand(specialty.getId()).toUri())
                .build();
    }

    @PutMapping("/specialties/{specialtyId}")
    public void updateSpecialty(@PathVariable int specialtyId, @RequestBody @Validated SpecialtyDto specialtyDto) {
        Specialty currentSpecialty = clinicService.findSpecialtyById(specialtyId);
        currentSpecialty.setName(specialtyDto.getName());
        clinicService.saveSpecialty(currentSpecialty);
    }

    @Transactional
    @DeleteMapping("/specialties/{specialtyId}")
    public void deleteSpecialty(@PathVariable int specialtyId) {
        Specialty specialty = clinicService.findSpecialtyById(specialtyId);
        clinicService.deleteSpecialty(specialty);
    }
}
