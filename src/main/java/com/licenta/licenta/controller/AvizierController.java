package com.licenta.licenta.controller;

import com.licenta.licenta.dto.AvizierDto;
import com.licenta.licenta.dto.CheltuialaDto;
import com.licenta.licenta.dto.ServiciuDto;
import com.licenta.licenta.service.AvizierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AvizierController {
    @Autowired
    public AvizierService avizierService;
    @GetMapping("${API-PATH}/asociatie/{asocId}/avizier/an/{anul}/luna/{luna}")
    public List<AvizierDto> getAvizier(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Integer anul, @PathVariable Integer luna ){
        return avizierService.getAvizier(auth2Authentication, asocId, anul, luna);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/avizier/an/{anul}/luna/{luna}/cheltuieli")
    public List<CheltuialaDto> getCheltuieli(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Integer anul, @PathVariable Integer luna ){
        return avizierService.getCheltuieli(auth2Authentication, asocId, anul, luna);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/avizier/an/{anul}/luna/{luna}/servicii")
    public List<ServiciuDto> getServicii(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Integer anul, @PathVariable Integer luna ){
        return avizierService.getServicii(auth2Authentication, asocId, anul, luna);
    }
    @GetMapping("${API-PATH}/apartament/{aptId}/avizier/an/{anul}/luna/{luna}")
    public List<AvizierDto> getAvizierLocatar(OAuth2Authentication auth2Authentication, @PathVariable Long aptId, @PathVariable Integer anul, @PathVariable Integer luna ){
        return avizierService.getAvizierLocatar(auth2Authentication, aptId, anul, luna);
    }
    @GetMapping("${API-PATH}/apartament/{aptId}/avizier/an/{anul}/luna/{luna}/cheltuieli")
    public List<CheltuialaDto> getCheltuieliLocatar(OAuth2Authentication auth2Authentication, @PathVariable Long aptId, @PathVariable Integer anul, @PathVariable Integer luna ){
        return avizierService.getCheltuieliLocatar(auth2Authentication, aptId, anul, luna);
    }
    @GetMapping("${API-PATH}/apartament/{aptId}/avizier/an/{anul}/luna/{luna}/servicii")
    public List<ServiciuDto> getServiciiLocatar(OAuth2Authentication auth2Authentication, @PathVariable Long aptId, @PathVariable Integer anul, @PathVariable Integer luna ){
        return avizierService.getServiciiLocatar(auth2Authentication, aptId, anul, luna);
    }
}
