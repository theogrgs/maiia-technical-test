package com.maiia.pro.controller;

import com.maiia.pro.dto.AvailabilityDTO;
import com.maiia.pro.service.ProAvailabilityService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/availabilities", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProAvailabilityController {
    private final ProAvailabilityService proAvailabilityService;

    public ProAvailabilityController(ProAvailabilityService proAvailabilityService) {
        this.proAvailabilityService = proAvailabilityService;
    }

    @ApiOperation(value = "Get availabilities by practitionerId")
    @GetMapping
    public List<AvailabilityDTO> getAvailabilities(@RequestParam final Integer practitionerId) {
        log.info("GET /availabilities?practitionerId={}", practitionerId);
        return proAvailabilityService.findByPractitionerId(practitionerId);
    }
}
