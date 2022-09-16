package com.licenta.licenta.controller;

import com.licenta.licenta.dto.ApartamentDto;
import com.licenta.licenta.dto.ApartamentLocatarDto;
import com.licenta.licenta.service.ApartamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApartamentController {

    @Autowired
    public ApartamentService apartamentService;
    @PostMapping("${API-PATH}/asociatie/{asocId}/apartament/creare")
    public ResponseEntity<Object> introducereApartament(OAuth2Authentication auth2Authentication, @RequestBody ApartamentDto apartamentDto, @PathVariable Long asocId){
        return apartamentService.createApartament(auth2Authentication, apartamentDto, asocId);
    }
    @GetMapping("${API-PATH}/asociatie/{id}/apartament")
    public List<ApartamentDto> getApartamentAll(OAuth2Authentication auth2Authentication, @PathVariable Long id){
        return apartamentService.getAll(auth2Authentication, id);
    }
    @PatchMapping("${API-PATH}/asociatie/{asocId}/apartament/{apId}")
    public ResponseEntity<Object>  actualizareApartament(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long apId, @RequestBody ApartamentDto apartamentDto){
        return apartamentService.actualizareApartament(auth2Authentication, asocId, apId, apartamentDto);
    }
    @DeleteMapping("${API-PATH}/asociatie/{asocId}/apartament/{apId}")
    public ResponseEntity<Object>  stergereApartament(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long apId){
        return apartamentService.stergereApartament(auth2Authentication, asocId, apId);
    }
    @GetMapping("${API-PATH}/locatar/apartament")
    public List<ApartamentLocatarDto> getApartamente(OAuth2Authentication auth2Authentication){
        return apartamentService.getApartamente(auth2Authentication);
    }
    @GetMapping("${API-PATH}/locatar/apartament/{id}")
    public ApartamentLocatarDto getApartament(OAuth2Authentication auth2Authentication, @PathVariable Long id){
        return apartamentService.getApartament(auth2Authentication, id);
    }
}
