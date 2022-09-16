package com.licenta.licenta.controller;
import com.licenta.licenta.dto.AsociatieCreareDto;
import com.licenta.licenta.dto.ServiciuDto;
import com.licenta.licenta.model.Asociatie;
import com.licenta.licenta.model.Serviciu;
import com.licenta.licenta.service.ServiciuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServiciiController {
    @Autowired
    ServiciuService serviciuService;
    @PostMapping("${API-PATH}/asociatie/servicii/creare")
    public ResponseEntity<Object> createServiciu(OAuth2Authentication auth2Authentication, @RequestBody ServiciuDto serviciuDto){
        return serviciuService.createServiciu(auth2Authentication, serviciuDto);
    }
    @GetMapping("${API-PATH}/asociatie/{id}/servicii")
    public List<ServiciuDto> getServiciiAll(OAuth2Authentication auth2Authentication, @PathVariable Long id){
        return serviciuService.getAll(auth2Authentication, id);
    }
    @PatchMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}")
    public ResponseEntity<Object>  actualizareServiciu(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long servId, @RequestBody ServiciuDto serviciuDto){
        return serviciuService.actualizareServiciu(auth2Authentication, asocId, servId, serviciuDto);
    }
    @DeleteMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}")
    public ResponseEntity<Object>  stergereServiciu(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long servId){
        return serviciuService.stergereServiciu(auth2Authentication, asocId, servId);
    }
}
