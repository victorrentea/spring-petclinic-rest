package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.rest.dto.VetDto;
import org.springframework.samples.petclinic.rest.dto.VetFieldsDto;

import java.util.Collection;
import java.util.List;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    @Mapping(target = "id", ignore = true)
    Vet toVet(VetFieldsDto vetFieldsDto);

    VetDto toVetDto(Vet vet);

    List<VetDto> toVetDtos(List<Vet> vets);
}
