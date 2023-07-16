package com.maiia.pro.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.maiia.pro.entity.Patient}
 */
@Value
public class PatientDTO implements Serializable {
    Integer id;
    String firstName;
    String lastName;
    LocalDate birthDate;
}
