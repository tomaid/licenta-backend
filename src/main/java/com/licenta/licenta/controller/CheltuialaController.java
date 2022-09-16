package com.licenta.licenta.controller;

import com.licenta.licenta.dto.CheltuialaDto;
import com.licenta.licenta.dto.FormulaCalculCheltuieliDto;
import com.licenta.licenta.service.CheltuialaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheltuialaController {

    @Autowired
    public CheltuialaService cheltuialaService;
    @PostMapping("${API-PATH}/asociatie/{asocId}/cheltuiala/introducere")
    public ResponseEntity<Object> introducereCheltuiala(OAuth2Authentication auth2Authentication, @RequestBody CheltuialaDto cheltuialaDto, @PathVariable Long asocId){
        return cheltuialaService.introducereCheltuiala(auth2Authentication, cheltuialaDto, asocId);
    }
    @GetMapping("${API-PATH}/asociatie/{id}/cheltuiala")
    public List<CheltuialaDto> getCheltuieli(OAuth2Authentication auth2Authentication, @PathVariable Long id){
        return cheltuialaService.getCheltuieli(auth2Authentication, id);
    }
    @PatchMapping("${API-PATH}/asociatie/{asocId}/cheltuiala/{cheltuialaId}")
    public ResponseEntity<Object>  actualizareCheltuiala(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long cheltuialaId, @RequestBody CheltuialaDto cheltuialaDto){
        return cheltuialaService.actualizareCheltuiala(auth2Authentication, asocId, cheltuialaId, cheltuialaDto);
    }
    @DeleteMapping("${API-PATH}/asociatie/{asocId}/cheltuiala/{cheltuialaId}")
    public ResponseEntity<Object>  stergereCheltuiala(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long cheltuialaId){
        return cheltuialaService.stergereCheltuiala(auth2Authentication, asocId, cheltuialaId);
    }
    @GetMapping("${API-PATH}/asociatie/{id}/cheltuiala/formula")
    public List<FormulaCalculCheltuieliDto> getFormule(OAuth2Authentication auth2Authentication){
        return cheltuialaService.getFormule(auth2Authentication);
    }
}

