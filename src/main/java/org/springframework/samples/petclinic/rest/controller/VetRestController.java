package org.springframework.samples.petclinic.rest.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.mapper.VetMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vets")
@RequiredArgsConstructor
@PreAuthorize("hasRole(@roles.VET_ADMIN)")
public class VetRestController {

    private final ClinicService clinicService;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @GetMapping
    public List<VetDto> listVets() {
        List<Vet> allVets = clinicService.findAllVets();
        return vetMapper.toVetDtos(allVets);
    }

    @GetMapping("{vetId}")
    public VetDto getVet(@PathVariable int vetId)  {
        Vet vet = clinicService.findVetById(vetId);
        return vetMapper.toVetDto(vet);
    }


    @PostMapping
    public ResponseEntity<Void> addVet(@RequestBody @Validated VetDto vetDto) {
        Vet vet = vetMapper.toVet(vetDto);
        if(vet.getNrOfSpecialties() > 0){
            List<Specialty> vetSpecialities = clinicService.findSpecialtiesByNameIn(vet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.toSet()));
            vet.setSpecialties(vetSpecialities);
        }
        clinicService.saveVet(vet);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/vets/{id}")
                        .buildAndExpand(vet.getId()).toUri())
                .build();
    }


    @PutMapping("{vetId}")
    public void updateVet(@PathVariable int vetId, @RequestBody VetDto vetDto)  {
        Vet currentVet = clinicService.findVetById(vetId);
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
    }

    @Transactional
    @DeleteMapping("{vetId}")
    public void deleteVet(@PathVariable int vetId) {
        Vet vet = clinicService.findVetById(vetId);
        clinicService.deleteVet(vet);
    }
}
