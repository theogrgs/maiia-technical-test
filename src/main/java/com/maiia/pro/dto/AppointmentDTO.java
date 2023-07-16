package com.maiia.pro.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.maiia.pro.entity.Appointment}
 */
@Value
public class AppointmentDTO implements Serializable {
    Integer id;
    Integer patientId;
    Integer practitionerId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}