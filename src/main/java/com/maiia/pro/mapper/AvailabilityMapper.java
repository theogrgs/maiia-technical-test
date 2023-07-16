package com.maiia.pro.mapper;

import com.maiia.pro.dto.AvailabilityDTO;
import com.maiia.pro.entity.Availability;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper extends AbstractMapper<Availability, AvailabilityDTO> {
    AvailabilityMapper INSTANCE = Mappers.getMapper(AvailabilityMapper.class);
}
