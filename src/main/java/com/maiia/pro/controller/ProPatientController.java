package com.maiia.pro.controller;

import com.maiia.pro.dto.PatientDTO;
import com.maiia.pro.service.ProPatientService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/patients", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProPatientController {
    private final ProPatientService proPatientService;

    public ProPatientController(ProPatientService proPatientService) {
        this.proPatientService = proPatientService;
    }

    @ApiOperation(value = "Get patients")
    @GetMapping
    public List<PatientDTO> getPatients() {
        log.info("GET /patients");
        return proPatientService.findAll();
    }
}
