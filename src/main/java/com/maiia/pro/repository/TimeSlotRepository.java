package com.maiia.pro.repository;

import com.maiia.pro.entity.TimeSlot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlot, String> {
    List<TimeSlot> findByPractitionerId(Integer practitionerId);

    /**
     * Find the time slot for appointment creation :
     * the time slot that starts before the appointment's start date and ends after the appointment's end date
     * @param practitionerId the practitioner id
     * @param appointmentEndDate the appointment end date
     * @param appointmentStartDate the appointment start date
     * @return The optional practitioner's time slot for the appointment
     */
    @Query("select t from TimeSlot t " +
            "where t.practitionerId = :practitionerId " +
            "and t.startDate <= :appointmentStartDate " +
            "and t.endDate >= :appointmentEndDate")
    Optional<TimeSlot> findForAppointmentCreation(@Param("practitionerId") Integer practitionerId,
                                                  @Param("appointmentEndDate") LocalDateTime appointmentEndDate,
                                                  @Param("appointmentStartDate") LocalDateTime appointmentStartDate);
}
