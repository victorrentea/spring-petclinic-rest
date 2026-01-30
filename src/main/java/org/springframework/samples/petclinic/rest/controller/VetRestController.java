package org.springframework.samples.petclinic.rest.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.mapper.VetMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.api.api.VetsApi;
import org.springframework.samples.petclinic.rest.api.dto.VetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController

@RequestMapping("api")
@RequiredArgsConstructor
public class VetRestController implements VetsApi {

    private final ClinicService clinicService;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Override
    public ResponseEntity<List<VetDto>> listVets() {
        List<VetDto> vets = new ArrayList<>(vetMapper.toVetDtos(clinicService.findAllVets()));
        if (vets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Override
    public ResponseEntity<VetDto> getVet(Integer vetId)  {
        Vet vet = clinicService.findVetById(vetId);
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vetMapper.toVetDto(vet), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Override
    public ResponseEntity<VetDto> addVet(VetDto vetDto) {
        HttpHeaders headers = new HttpHeaders();
        Vet vet = vetMapper.toVet(vetDto);
        if(vet.getNrOfSpecialties() > 0){
            List<Specialty> vetSpecialities = clinicService.findSpecialtiesByNameIn(vet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.toSet()));
            vet.setSpecialties(vetSpecialities);
        }
        clinicService.saveVet(vet);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/vets/{id}").buildAndExpand(vet.getId()).toUri());
        return new ResponseEntity<>(vetMapper.toVetDto(vet), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Override
    public ResponseEntity<VetDto> updateVet(Integer vetId,VetDto vetDto)  {
        Vet currentVet = clinicService.findVetById(vetId);
        if (currentVet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentVet.setFirstName(vetDto.getFirstName());
        currentVet.setLastName(vetDto.getLastName());
        currentVet.clearSpecialties();
        for (Specialty spec : specialtyMapper.toSpecialty(vetDto.getSpecialties())) {
            currentVet.addSpecialty(spec);
        }
        if(currentVet.getNrOfSpecialties() > 0){
            List<Specialty> vetSpecialities = clinicService.findSpecialtiesByNameIn(currentVet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.toSet()));
            currentVet.setSpecialties(vetSpecialities);
        }
        clinicService.saveVet(currentVet);
        return new ResponseEntity<>(vetMapper.toVetDto(currentVet), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    @Override
    public ResponseEntity<VetDto> deleteVet(Integer vetId) {
        Vet vet = clinicService.findVetById(vetId);
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deleteVet(vet);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
