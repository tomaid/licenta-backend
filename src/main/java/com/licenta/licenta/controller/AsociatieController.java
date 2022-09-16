package com.licenta.licenta.controller;

import com.licenta.licenta.dto.AsociatieCreareDto;
import com.licenta.licenta.dto.AsociatieDetaliiCuJudetDto;
import com.licenta.licenta.dto.AsociatieDetaliiDto;
import com.licenta.licenta.dto.AsociatieDto;
import com.licenta.licenta.service.AsociatieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AsociatieController {
    @Autowired
    public AsociatieService asociatieService;
    @PostMapping("${API-PATH}/asociatie/create")
    public ResponseEntity<Object> createAsociatie(OAuth2Authentication auth2Authentication, @RequestBody AsociatieCreareDto asociatieCreareDto){
        return asociatieService.creareAsociatie(auth2Authentication, asociatieCreareDto);
    }
    @GetMapping("${API-PATH}/asociatie/getAll")
    public List<AsociatieDto> getAll(OAuth2Authentication auth2Authentication) {
        return asociatieService.getAll(auth2Authentication);
    }
    @GetMapping("${API-PATH}/asociatie/{id}")
    public AsociatieDetaliiDto getAll(OAuth2Authentication auth2Authentication, @PathVariable Long id) throws ResponseStatusException {
        return asociatieService.getAsociatie(auth2Authentication, id);
    }
    @GetMapping("${API-PATH}/asociatie/{id}/date-factura")
    public AsociatieDetaliiCuJudetDto getDateFactura(OAuth2Authentication auth2Authentication, @PathVariable Long id) throws ResponseStatusException {
        return asociatieService.getAsociatieFactura(auth2Authentication, id);
    }
    @PatchMapping("${API-PATH}/asociatie/{id}")
    public ResponseEntity<Object>  actualizareAsociatie(OAuth2Authentication auth2Authentication, @PathVariable Long id, @RequestBody AsociatieCreareDto asociatieCreareDto) {
        return asociatieService.actualizareAsociatie(auth2Authentication, id, asociatieCreareDto);
    }
    @GetMapping("${API-PATH}/apartament/{id}/date-factura")
    public AsociatieDetaliiCuJudetDto getDateFacturaPentruLocatar(OAuth2Authentication auth2Authentication,  @PathVariable Long id) throws ResponseStatusException {
        return asociatieService.getDateFacturaPentruLocatar(auth2Authentication, id);
    }

}
