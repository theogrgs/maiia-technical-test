package com.maiia.pro.mapper;

import com.maiia.pro.dto.TimeSlotDTO;
import com.maiia.pro.entity.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper extends AbstractMapper<TimeSlot, TimeSlotDTO>{
    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

}
