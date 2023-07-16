package com.maiia.pro.service;

import com.maiia.pro.dto.TimeSlotDTO;
import com.maiia.pro.mapper.TimeSlotMapper;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProTimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public ProTimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    // Maybe delete this function since it is not used. However, I guess it is used in the real code
    public List<TimeSlotDTO> findByPractitionerId(Integer practitionerId) {
        return TimeSlotMapper.INSTANCE.toDto(timeSlotRepository.findByPractitionerId(practitionerId));
    }
}
