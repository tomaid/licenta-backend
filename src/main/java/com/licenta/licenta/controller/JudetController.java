package com.licenta.licenta.controller;


import com.licenta.licenta.dto.JudetDto;
import com.licenta.licenta.service.JudetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JudetController {
    @Autowired
    JudetService judetService;
    @GetMapping("${API-PATH}/judete/getAll")
    public List<JudetDto> getAll(OAuth2Authentication auth2Authentication) {
        return judetService.getAll(auth2Authentication);
    }
}
