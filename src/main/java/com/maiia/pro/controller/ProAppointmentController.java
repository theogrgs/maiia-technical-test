package com.maiia.pro.controller;

import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.service.ProAppointmentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProAppointmentController {
    private final ProAppointmentService proAppointmentService;

    public ProAppointmentController(ProAppointmentService proAppointmentService) {
        this.proAppointmentService = proAppointmentService;
    }

    @ApiOperation(value = "Get appointments by practitionerId")
    @GetMapping
    public List<AppointmentDTO> getAppointmentsByPractitioner(@RequestParam final Integer practitionerId) {
        log.info("GET /appointments?practitionerId={}", practitionerId);
        return proAppointmentService.findByPractitionerId(practitionerId);
    }

    @ApiOperation(value = "Get all appointments")
    @GetMapping
    public List<AppointmentDTO> getAppointments() {
        log.info("GET /appointments");
        return proAppointmentService.findAll();
    }

    @ApiOperation(value = "Create a new appointment")
    @PostMapping
    public AppointmentDTO createAppointment(@RequestBody final AppointmentDTO appointmentDTO) {
        log.info("POST /appointments, Availability: {}", appointmentDTO.toString());
        return proAppointmentService.createAppointment(appointmentDTO);
    }
}
