package com.licenta.licenta.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PreAuthorize("hasAnyAuthority('LOCATAR','ADMIN')")
    @GetMapping("${API-PATH}/user/**")
    public String getUserAccess() {
        return "user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("${API-PATH}/admin/**")
    public String getAdminAccess() {
        return "admin";
    }
}
