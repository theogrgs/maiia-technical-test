package com.maiia.pro.controller;

import com.maiia.pro.dto.PractitionerDTO;
import com.maiia.pro.service.ProPractitionerService;
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
@RequestMapping(value = "/practitioners", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProPractitionerController {
    private final ProPractitionerService proPractitionerService;

    public ProPractitionerController(ProPractitionerService proPractitionerService) {
        this.proPractitionerService = proPractitionerService;
    }

    @ApiOperation(value = "Get practitioners")
    @GetMapping
    public List<PractitionerDTO> getPractitioners() {
        log.info("GET /practitioners");
        return proPractitionerService.findAll();
    }
}
