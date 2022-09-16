package com.licenta.licenta.controller;

import com.licenta.licenta.dto.*;
import com.licenta.licenta.service.FacturaService;
import com.licenta.licenta.service.PlataFacturaDeLocatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FacturaController {

    @Autowired
    public FacturaService facturaService;
    @GetMapping("${API-PATH}/asociatie/{asocId}/factura")
    public List<FacturaTabelDto> getFacturiAll(OAuth2Authentication auth2Authentication, @PathVariable Long asocId){
        return facturaService.getAll(auth2Authentication, asocId);
    }
    @PostMapping("${API-PATH}/asociatie/{asocId}/factura/generare")
    public ResponseEntity<Object> generareFacturi(OAuth2Authentication auth2Authentication, @RequestBody GenerareFacturi generareFacturi, @PathVariable Long asocId){
        return facturaService.generareFacturi(auth2Authentication, generareFacturi, asocId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/factura/{facturaId}")
    public FacturaDateDto getFactura(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long facturaId ){
        return facturaService.getFactura(auth2Authentication, asocId, facturaId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/factura/{facturaId}/detalii")
    public List<FacturaDateDetaliiDto> getFacturaDetalii(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long facturaId ){
        return facturaService.getFacturaDetalii(auth2Authentication, asocId, facturaId);
    }
    @PostMapping("${API-PATH}/asociatie/{asocId}/factura/{facturaId}/plata")
    public ResponseEntity<Object> plataFactura(OAuth2Authentication auth2Authentication, @RequestBody PlataFacturaDto plataFacturaDto, @PathVariable Long asocId,  @PathVariable Long facturaId){
        return facturaService.plataFactura(auth2Authentication, plataFacturaDto, asocId, facturaId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/factura/{facturaId}/chitanta/all")
    public List<ChitanteDto> getFacturaChitante(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long facturaId ){
        return facturaService.getFacturaChitante(auth2Authentication, asocId, facturaId);
    }
    @GetMapping("${API-PATH}/apartament/{apartamentId}/factura")
    public List<FacturaTabelDto> getFacturiApartament(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId){
        return facturaService.getFacturiApartament(auth2Authentication, apartamentId);
    }
    @GetMapping("${API-PATH}/apartament/{apartamentId}/factura/{facturaId}")
    public FacturaDateDto getFacturaApartament(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId, @PathVariable Long facturaId ){
        return facturaService.getFacturaApartament(auth2Authentication, apartamentId, facturaId);
    }
    @GetMapping("${API-PATH}/apartament/{apartamentId}/factura/{facturaId}/detalii")
    public List<FacturaDateDetaliiDto> getFacturaApartamentDetalii(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId, @PathVariable Long facturaId ){
        return facturaService.getFacturaApartamentDetalii(auth2Authentication, apartamentId, facturaId);
    }
    @PostMapping("${API-PATH}/apartament/{apartamentId}/factura/{facturaId}/plata")
    public ResponseEntity<Object> plataFacturaApartament(OAuth2Authentication auth2Authentication, @RequestBody PlataFacturaDeLocatar plataFacturaDto, @PathVariable Long apartamentId, @PathVariable Long facturaId){
        return facturaService.plataFacturaApartament(auth2Authentication, plataFacturaDto, apartamentId, facturaId);
    }
    @GetMapping("${API-PATH}/apartament/{apartamentId}/factura/{facturaId}/chitanta/all")
    public List<ChitanteDto> getFacturaChitanteApartament(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId, @PathVariable Long facturaId ){
        return facturaService.getFacturaChitanteApartament(auth2Authentication, apartamentId, facturaId);
    }

}
