package com.revpasswordmanager.generator_service.controller;

import com.revpasswordmanager.generator_service.service.PasswordGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
public class GeneratorController {

    @Autowired
    private PasswordGeneratorService generatorService;

    @GetMapping
    public String generatePassword(
            @RequestParam int length,
            @RequestParam boolean upper,
            @RequestParam boolean numbers,
            @RequestParam boolean symbols) {

        return generatorService.generatePassword(length, upper, numbers, symbols);
    }
}