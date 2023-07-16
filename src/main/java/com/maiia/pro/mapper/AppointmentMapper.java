package com.maiia.pro.mapper;

import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper extends AbstractMapper<Appointment, AppointmentDTO> {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

}
