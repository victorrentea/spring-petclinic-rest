package org.springframework.samples.petclinic.rest.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SpecialtyRestController {

    private final ClinicService clinicService;

    private final SpecialtyMapper specialtyMapper;

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @GetMapping(value = "/specialties", produces = "application/json")
    public ResponseEntity<List<SpecialtyDto>> listSpecialties() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.addAll(specialtyMapper.toSpecialtyDtos(clinicService.findAllSpecialties()));
        if (specialties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @GetMapping(value = "/specialties/{specialtyId}", produces = "application/json")
    public ResponseEntity<SpecialtyDto> getSpecialty(@PathVariable Integer specialtyId) {
        Specialty specialty = clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PostMapping(value = "/specialties", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SpecialtyDto> addSpecialty(@RequestBody SpecialtyDto specialtyDto) {
        HttpHeaders headers = new HttpHeaders();
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        clinicService.saveSpecialty(specialty);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/specialties/{id}").buildAndExpand(specialty.getId()).toUri());
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PutMapping(value = "/specialties/{specialtyId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SpecialtyDto> updateSpecialty(@PathVariable Integer specialtyId, @RequestBody SpecialtyDto specialtyDto) {
        Specialty currentSpecialty = clinicService.findSpecialtyById(specialtyId);
        if (currentSpecialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentSpecialty.setName(specialtyDto.getName());
        clinicService.saveSpecialty(currentSpecialty);
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(currentSpecialty), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    @DeleteMapping(value = "/specialties/{specialtyId}", produces = "application/json")
    public ResponseEntity<SpecialtyDto> deleteSpecialty(@PathVariable Integer specialtyId) {
        Specialty specialty = clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deleteSpecialty(specialty);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
