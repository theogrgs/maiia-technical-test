package com.maiia.pro.service;

import com.maiia.pro.EntityFactory;
import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.Practitioner;
import com.maiia.pro.mapper.AppointmentMapper;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.PractitionerRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProAppointmentServiceTest {
    private final EntityFactory entityFactory = new EntityFactory();
    private final static Integer patient_id = 657679;

    @Autowired
    private ProAppointmentService proAppointmentService;

    @Autowired
    private PractitionerRepository practitionerRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    private static void assertAppointmentCreatedOK(Practitioner practitioner, LocalDateTime startDate, AppointmentDTO appointmentCreated) {
        assertNotNull(appointmentCreated.getId());
        assertEquals(practitioner.getId(), appointmentCreated.getPractitionerId());
        assertEquals(patient_id, appointmentCreated.getPatientId());
        assertEquals(startDate, appointmentCreated.getStartDate());
        assertEquals(startDate.plusMinutes(15), appointmentCreated.getEndDate());
    }

    @Test
    void createAppointent() {
        Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        timeSlotRepository.save(entityFactory.createTimeSlot(practitioner.getId(), startDate, startDate.plusHours(1)));

        Appointment appointment = entityFactory.createAppointment(practitioner.getId(), patient_id, startDate, startDate.plusMinutes(15));
        AppointmentDTO appointmentDTO = AppointmentMapper.INSTANCE.toDto(appointment);
        AppointmentDTO appointmentCreated = proAppointmentService.createAppointment(appointmentDTO);

        assertAppointmentCreatedOK(practitioner, startDate, appointmentCreated);
    }

    @Test
    void createAppointmentOverlappingAvailability() {
        Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        timeSlotRepository.save(entityFactory.createTimeSlot(practitioner.getId(), startDate, startDate.plusHours(1)));
        availabilityRepository.save(Availability.builder().practitionerId(practitioner.getId()).startDate(startDate).endDate(startDate.plusMinutes(15)).build());

        Appointment appointment = entityFactory.createAppointment(practitioner.getId(), patient_id, startDate, startDate.plusMinutes(15));
        AppointmentDTO appointmentDTO = AppointmentMapper.INSTANCE.toDto(appointment);
        AppointmentDTO appointmentCreated = proAppointmentService.createAppointment(appointmentDTO);

        assertAppointmentCreatedOK(practitioner, startDate, appointmentCreated);
        List<Availability> availabilities = availabilityRepository.findByPractitionerId(practitioner.getId());
        assertEquals(0, availabilities.size());
    }

    @Test
    void createAppointmentOverlappingManyAvailabilities() {
        Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);
        timeSlotRepository.save(entityFactory.createTimeSlot(practitioner.getId(), startDate, startDate.plusHours(1)));
        availabilityRepository.save(Availability.builder().practitionerId(practitioner.getId()).startDate(startDate.minusMinutes(10)).endDate(startDate.plusMinutes(5)).build());
        availabilityRepository.save(Availability.builder().practitionerId(practitioner.getId()).startDate(startDate.plusMinutes(10)).endDate(startDate.plusMinutes(25)).build());

        Appointment appointment = entityFactory.createAppointment(practitioner.getId(), patient_id, startDate, startDate.plusMinutes(15));
        AppointmentDTO appointmentDTO = AppointmentMapper.INSTANCE.toDto(appointment);
        AppointmentDTO appointmentCreated = proAppointmentService.createAppointment(appointmentDTO);

        assertAppointmentCreatedOK(practitioner, startDate, appointmentCreated);
        List<Availability> availabilities = availabilityRepository.findByPractitionerId(practitioner.getId());
        assertEquals(0, availabilities.size());
    }

    @Test
    void createAppointentWithoutTimeSlot() {
        Practitioner practitioner = practitionerRepository.save(entityFactory.createPractitioner());
        LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 5, 11, 0, 0);

        Appointment appointment = entityFactory.createAppointment(practitioner.getId(), patient_id, startDate, startDate.plusMinutes(15));
        AppointmentDTO appointmentDTO = AppointmentMapper.INSTANCE.toDto(appointment);
        AppointmentDTO appointmentCreated = proAppointmentService.createAppointment(appointmentDTO);

        assertNull(appointmentCreated);
    }
}
