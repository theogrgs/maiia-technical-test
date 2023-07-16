package com.maiia.pro.mapper;

import com.maiia.pro.dto.PractitionerDTO;
import com.maiia.pro.entity.Practitioner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PractitionerMapper extends AbstractMapper<Practitioner, PractitionerDTO> {
    PractitionerMapper INSTANCE = Mappers.getMapper(PractitionerMapper.class);
}
