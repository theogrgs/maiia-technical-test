package com.maiia.pro.service;

import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.mapper.AppointmentMapper;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final TimeSlotRepository timeSlotRepository;

    public ProAppointmentService(
            AppointmentRepository appointmentRepository,
            AvailabilityRepository availabilityRepository,
            TimeSlotRepository timeSlotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    // Maybe delete this function since it is not used. However, I guess it is used in the real code
    public AppointmentDTO find(String appointmentId) {
        return AppointmentMapper.INSTANCE.toDto(appointmentRepository.findById(appointmentId).orElseThrow());
    }

    public List<AppointmentDTO> findAll() {
        return AppointmentMapper.INSTANCE.toDto(appointmentRepository.findAll());
    }

    public List<AppointmentDTO> findByPractitionerId(Integer practitionerId) {
        return AppointmentMapper.INSTANCE.toDto(appointmentRepository.findByPractitionerId(practitionerId));
    }

    /**
     * Create an appointment for a practitioner
     * deletes all availabilities overlapping the appointment
     * @param appointmentDTO the appointment to create
     * @return the created appointment
     */
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointmentToCreate = AppointmentMapper.INSTANCE.toEntity(appointmentDTO);
        if (checkAppointmentForCreation(appointmentToCreate)) {
            availabilityRepository.deleteAvailabilitiesToDeleteForAppointment(
                    appointmentToCreate.getPractitionerId(),
                    appointmentToCreate.getEndDate(),
                    appointmentToCreate.getStartDate()
            );
            return AppointmentMapper.INSTANCE.toDto(appointmentRepository.save(appointmentToCreate));
        }
        return null;
    }

    /**
     * Checks whether an appointment can be created :
     * if the practitioner has a timeslot that starts before and ends after the appointment
     * @param appointment The appointment to create
     * @return true/false whether the appointment can be created
     */
    private boolean checkAppointmentForCreation(Appointment appointment) {
        Optional<TimeSlot> appointmentPractitionnerTimeSlot = timeSlotRepository.findForAppointmentCreation(appointment.getPractitionerId(), appointment.getEndDate(), appointment.getStartDate());
        return appointmentPractitionnerTimeSlot.isPresent();
    }
}
