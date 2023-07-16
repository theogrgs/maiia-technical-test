package com.maiia.pro.mapper;

import com.maiia.pro.dto.PatientDTO;
import com.maiia.pro.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientMapper extends AbstractMapper<Patient, PatientDTO>{
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

}
