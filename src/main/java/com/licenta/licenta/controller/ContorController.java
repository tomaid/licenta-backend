package com.licenta.licenta.controller;

import com.licenta.licenta.dto.ContorDto;
import com.licenta.licenta.service.ContorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContorController {
    @Autowired
    public ContorService contorService;
    @PostMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}/contor/creare")
    public ResponseEntity<Object> createContor(OAuth2Authentication auth2Authentication, @RequestBody ContorDto contorDto, @PathVariable Long asocId, @PathVariable Long servId){
        return contorService.createContor(auth2Authentication, contorDto, asocId, servId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}/contor/all")
    public List<ContorDto> getApartamentAll(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long servId){
        return contorService.getAll(auth2Authentication, asocId,servId);
    }
    @PatchMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}/contor/{contorId}")
    public ResponseEntity<Object>  actualizareContor(OAuth2Authentication auth2Authentication,@PathVariable Long asocId, @PathVariable Long servId, @PathVariable Long contorId, @RequestBody ContorDto contorDto){
        return contorService.actualizareContor(auth2Authentication, asocId, servId, contorId, contorDto);
    }
    @DeleteMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}/contor/{contorId}")
    public ResponseEntity<Object>  stergereContor(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long servId, @PathVariable Long contorId){
        return contorService.stergereContor(auth2Authentication, asocId, servId, contorId);
    }



}
