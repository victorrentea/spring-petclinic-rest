package org.springframework.samples.petclinic.rest.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitRestController {

    private final ClinicService clinicService;

    private final VisitMapper visitMapper;


    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<VisitDto>> listVisits() {
        List<Visit> visits = new ArrayList<>(clinicService.findAllVisits());
        if (visits.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ArrayList<>(visitMapper.toVisitsDto(visits)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @GetMapping(value = "/{visitId}", produces = "application/json")
    public ResponseEntity<VisitDto> getVisit(@PathVariable("visitId") Integer visitId) {
        Visit visit = clinicService.findVisitById(visitId);
        if (visit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(visitMapper.toVisitDto(visit), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<VisitDto> addVisit(@RequestBody VisitDto visitDto) {
        HttpHeaders headers = new HttpHeaders();
        Visit visit = visitMapper.toVisit(visitDto);
        clinicService.saveVisit(visit);
        visitDto = visitMapper.toVisitDto(visit);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.getId()).toUri());
        return new ResponseEntity<>(visitDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @PutMapping(value = "/{visitId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<VisitDto> updateVisit(@PathVariable("visitId") Integer visitId, @RequestBody VisitFieldsDto visitDto) {
        Visit currentVisit = clinicService.findVisitById(visitId);
        if (currentVisit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentVisit.setDate(visitDto.getDate());
        currentVisit.setDescription(visitDto.getDescription());
        clinicService.saveVisit(currentVisit);
        return new ResponseEntity<>(visitMapper.toVisitDto(currentVisit), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Transactional
    @DeleteMapping(value = "/{visitId}", produces = "application/json")
    public ResponseEntity<VisitDto> deleteVisit(@PathVariable("visitId") Integer visitId) {
        Visit visit = clinicService.findVisitById(visitId);
        if (visit == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clinicService.deleteVisit(visit);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
