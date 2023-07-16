package com.maiia.pro.repository;

import com.maiia.pro.entity.Availability;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends CrudRepository<Availability, Integer> {
    List<Availability> findByPractitionerId(Integer id);

    /**
     * Deletes availabilities that overlaps the appointment to create for a practitioner
     * @param practitionerId the practitioner id
     * @param appointmentEndDate the appointment end date
     * @param appointmentStartDate the appointment start date
     */
    @Transactional
    @Modifying
    @Query("delete from Availability a " +
            "where a.practitionerId = :practitionerId " +
            "and ( " +
                "(a.startDate <= :appointmentEndDate and a.startDate >= :appointmentStartDate) " +
                "or (a.endDate >= :appointmentStartDate and a.endDate <= :appointmentEndDate)" +
            ")")
    void deleteAvailabilitiesToDeleteForAppointment(
            @Param("practitionerId") Integer practitionerId,
            @Param("appointmentEndDate") LocalDateTime appointmentEndDate,
            @Param("appointmentStartDate") LocalDateTime appointmentStartDate
    );

}
