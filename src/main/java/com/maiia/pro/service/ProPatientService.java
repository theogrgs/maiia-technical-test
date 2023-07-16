package com.maiia.pro.service;

import com.maiia.pro.dto.PatientDTO;
import com.maiia.pro.mapper.PatientMapper;
import com.maiia.pro.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProPatientService {
    private final PatientRepository patientRepository;

    public ProPatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Maybe delete this function since it is not used. However, I guess it is used in the real code
    public PatientDTO find(String patientId) {
        return PatientMapper.INSTANCE.toDto(patientRepository.findById(patientId).orElseThrow());
    }

    public List<PatientDTO> findAll() {
        return PatientMapper.INSTANCE.toDto(patientRepository.findAll());
    }
}
