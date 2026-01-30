package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;

import java.util.Collection;
import java.util.List;

/**
 * Map Specialty & SpecialtyDto using mapstruct
 */
@Mapper
public interface SpecialtyMapper {
    Specialty toSpecialty(SpecialtyDto specialtyDto);

    SpecialtyDto toSpecialtyDto(Specialty specialty);

    List<SpecialtyDto> toSpecialtyDtos(List<Specialty> specialties);

    List<Specialty> toSpecialtys(List<SpecialtyDto> specialties);

}
