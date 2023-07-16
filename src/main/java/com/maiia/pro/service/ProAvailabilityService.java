package com.maiia.pro.service;

import com.maiia.pro.dto.AvailabilityDTO;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.mapper.AvailabilityMapper;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProAvailabilityService {

    public static final long AVAILABILITY_DURATION_MINUTES = 15;
    private final AvailabilityRepository availabilityRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;

    public ProAvailabilityService(
            AvailabilityRepository availabilityRepository,
            TimeSlotRepository timeSlotRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.availabilityRepository = availabilityRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Retrieves the availabilities for a given practitioner ID.
     *
     * @param practitionerId The ID of the practitioner.
     * @return A list of availabilities.
     */
    public List<AvailabilityDTO> findByPractitionerId(Integer practitionerId) {
        return AvailabilityMapper.INSTANCE.toDto(availabilityRepository.findByPractitionerId(practitionerId));
    }

    /**
     * Generates availabilities for a given practitioner ID.
     *
     * @param practitionerId The ID of the practitioner.
     * @return The list of generated availabilities.
     */
    public List<Availability> generateAvailabilities(Integer practitionerId) {
        // Retrieve the time slots for the practitioner
        List<TimeSlot> practitionerTimeSlots = timeSlotRepository.findByPractitionerId(practitionerId);

        // Retrieve the appointments for the practitioner
        List<Appointment> practitionerAppointments = appointmentRepository.findByPractitionerId(practitionerId);

        // Retrieve the existing availabilities for the practitioner
        List<Availability> practitionerAvailabilities = availabilityRepository.findByPractitionerId(practitionerId);

        // List to store the newly created availabilities
        List<Availability> availabilitiesToCreate = new ArrayList<>();

        // Sort the time slots and appointments based on their start dates
        practitionerTimeSlots.sort(Comparator.comparing(TimeSlot::getStartDate));
        practitionerAppointments.sort(Comparator.comparing(Appointment::getStartDate));

        // Iterate over each time slot for the practitioner
        for (TimeSlot timeSlot : practitionerTimeSlots) {
            LocalDateTime timeSlotStartDate = timeSlot.getStartDate();
            LocalDateTime timeSlotEndDate = timeSlot.getEndDate();

            LocalDateTime previousAppointmentEnd = timeSlotStartDate;
            LocalDateTime availabilityStart = timeSlotStartDate;

            // Iterate over each appointment for the practitioner within the current time slot
            for (Appointment appointment : practitionerAppointments) {

                // Fill the gaps between appointments with availabilities of AVAILABILITY_DURATION_MINUTES minutes
                while (appointment.getStartDate().minusMinutes(AVAILABILITY_DURATION_MINUTES).isAfter(availabilityStart)
                        || appointment.getStartDate().minusMinutes(AVAILABILITY_DURATION_MINUTES).isEqual(availabilityStart)) {
                    addAvailabilityIfYouCan(
                            practitionerAvailabilities,
                            availabilitiesToCreate,
                            availabilityStart,
                            availabilityStart.plusMinutes(AVAILABILITY_DURATION_MINUTES),
                            practitionerId
                    );
                    availabilityStart = availabilityStart.plusMinutes(AVAILABILITY_DURATION_MINUTES);
                }

                // Update the availability start time if the appointment ends before the time slot end time
                if (appointment.getEndDate().isBefore(timeSlotEndDate)) {
                    availabilityStart = appointment.getEndDate();
                }

                // Update the end time of the last appointment within the time slot
                if (appointment.getEndDate().isAfter(previousAppointmentEnd)) {
                    previousAppointmentEnd = appointment.getEndDate();
                }
            }

            // Check the end of the time slot for any remaining available time
            if (timeSlotEndDate.isAfter(previousAppointmentEnd)) {
                long minutesBeforeTimeSlotEnd = Duration.between(previousAppointmentEnd, timeSlotEndDate).toMinutes();
                if (minutesBeforeTimeSlotEnd >= AVAILABILITY_DURATION_MINUTES) {
                    availabilityStart = previousAppointmentEnd;

                    // Create availabilities for the remaining time within the time slot
                    while (availabilityStart.plusMinutes(AVAILABILITY_DURATION_MINUTES).isBefore(timeSlotEndDate)
                            || availabilityStart.plusMinutes(AVAILABILITY_DURATION_MINUTES).isEqual(timeSlotEndDate)) {
                        LocalDateTime availabilityEnd = availabilityStart.plusMinutes(AVAILABILITY_DURATION_MINUTES);
                        addAvailabilityIfYouCan(
                                practitionerAvailabilities,
                                availabilitiesToCreate,
                                availabilityStart,
                                availabilityEnd,
                                practitionerId
                        );
                        availabilityStart = availabilityEnd;
                    }

                    // Create an additional availability if there is still time remaining before the end of the time slot
                    if (availabilityStart.isBefore(timeSlotEndDate)) {
                        addAvailabilityIfYouCan(
                                practitionerAvailabilities,
                                availabilitiesToCreate,
                                availabilityStart,
                                availabilityStart.plusMinutes(AVAILABILITY_DURATION_MINUTES),
                                practitionerId
                        );
                    }
                }
            }
        }

        // Save the newly created availabilities
        Iterable<Availability> availabilitiesCreated = availabilityRepository.saveAll(availabilitiesToCreate);

        // Return the list of newly created availabilities
        return StreamSupport.stream(availabilitiesCreated.spliterator(), false).collect(Collectors.toList());

    }

    /**
     * Adds an availability to the list of availabilities to create if it is not already present.
     *
     * @param practitionerAvailabilities The existing availabilities of the practitioner.
     * @param availabilitiesToCreate     The list of availabilities to create.
     * @param availabilityStartDate      The start date of the availability.
     * @param availabilityEndDate        The end date of the availability.
     * @param practitionerId             The ID of the practitioner.
     */
    private static void addAvailabilityIfYouCan(
            List<Availability> practitionerAvailabilities,
            List<Availability> availabilitiesToCreate,
            LocalDateTime availabilityStartDate,
            LocalDateTime availabilityEndDate,
            Integer practitionerId
    ) {
        // Check if the start date or end date of the availability being created already exists in the existing practitioner availabilities
        for (Availability disponibilite : practitionerAvailabilities) {
            if (disponibilite.getStartDate().isEqual(availabilityStartDate) || disponibilite.getEndDate().isEqual(availabilityEndDate)) {
                return; // Avoid adding duplicate availabilities
            }
        }

        // Check if the start date or end date of the availability being created already exists in the availabilities to be created
        for (Availability availability : availabilitiesToCreate) {
            if (availability.getStartDate().isEqual(availabilityStartDate) || availability.getEndDate().isEqual(availabilityEndDate)) {
                return; // Avoid adding duplicate availabilities
            }
        }

        // Create a new availability with the provided start date, end date, and practitioner ID
        Availability availabilityToCreate = Availability.builder()
                .startDate(availabilityStartDate)
                .endDate(availabilityEndDate)
                .practitionerId(practitionerId)
                .build();

        // Add the newly created availability to the list of availabilities to be created
        availabilitiesToCreate.add(availabilityToCreate);
    }
}
