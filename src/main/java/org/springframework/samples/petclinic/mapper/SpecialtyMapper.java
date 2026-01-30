package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.api.dto.SpecialtyDto;

import java.util.List;

@Mapper
public interface SpecialtyMapper {
    Specialty toSpecialty(SpecialtyDto specialtyDto);

    SpecialtyDto toSpecialtyDto(Specialty specialty);

    List<SpecialtyDto> toSpecialtyDtos(List<Specialty> specialties);

    List<Specialty> toSpecialty(List<SpecialtyDto> specialties);

}
