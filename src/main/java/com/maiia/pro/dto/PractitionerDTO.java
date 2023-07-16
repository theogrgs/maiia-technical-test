package com.maiia.pro.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.maiia.pro.entity.Practitioner}
 */
@Value
public class PractitionerDTO implements Serializable {
    Integer id;
    String firstName;
    String lastName;
    String speciality;
}