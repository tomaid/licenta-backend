package com.licenta.licenta.controller;
import com.licenta.licenta.dto.*;
import com.licenta.licenta.model.Mesaj;
import com.licenta.licenta.model.User;
import com.licenta.licenta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    @Autowired
    public UserService userService;
    // (path = "${API-PATH}/user/create", produces = "application/json")
    @PostMapping("${API-PATH}/user/create")
    public ResponseEntity<Mesaj> createUser(@RequestBody UserRegistrationDto userRegistrationDto){
        return userService.creareUser(userRegistrationDto);
    }
    @GetMapping("${API-PATH}/user/actualizare-date")
    public UserActualizareDto getProfile(OAuth2Authentication auth2Authentication) {
        return userService.getProfile(auth2Authentication);
    }
    @PutMapping("${API-PATH}/user/actualizare-date")
    public ResponseEntity<Object> actualizareDate(OAuth2Authentication auth2Authentication, @RequestBody UserActualizareDto userActualizareDto) {
        return userService.actualizareDate(auth2Authentication, userActualizareDto);
    }
    @PatchMapping("${API-PATH}/user/actualizare-parola")
    public ResponseEntity<Object> actualizareParola(OAuth2Authentication auth2Authentication, @RequestBody UserActualizareParolaDto userActualizareParolaDto) {
        return userService.actualizareParola(auth2Authentication, userActualizareParolaDto);
    }
    @PostMapping("${API-PATH}/user/reset-parola")
    public ResponseEntity<Mesaj> resetareParola(@RequestBody ResetParolaDto resetParolaDto) {
        return userService.resetareParola(resetParolaDto);
    }
    @GetMapping("${API-PATH}/user/role")
    public RoleDto getRole(OAuth2Authentication auth2Authentication) {
        return userService.getRole(auth2Authentication);
    }
}
