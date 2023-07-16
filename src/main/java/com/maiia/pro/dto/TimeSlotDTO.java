package com.maiia.pro.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.maiia.pro.entity.TimeSlot}
 */
@Value
public class TimeSlotDTO implements Serializable {
    Integer id;
    Integer practitionerId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
