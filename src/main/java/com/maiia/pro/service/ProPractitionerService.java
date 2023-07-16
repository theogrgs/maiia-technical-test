package com.maiia.pro.service;

import com.maiia.pro.dto.PractitionerDTO;
import com.maiia.pro.mapper.PractitionerMapper;
import com.maiia.pro.repository.PractitionerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProPractitionerService {
    private final PractitionerRepository practitionerRepository;

    public ProPractitionerService(PractitionerRepository practitionerRepository) {
        this.practitionerRepository = practitionerRepository;
    }

    // Maybe delete this function since it is not used. However, I guess it is used in the real code
    public PractitionerDTO find(Integer practitionerId) {
        return PractitionerMapper.INSTANCE.toDto(practitionerRepository.findById(practitionerId).orElseThrow());
    }

    public List<PractitionerDTO> findAll() {
        return PractitionerMapper.INSTANCE.toDto(practitionerRepository.findAll());
    }
}
