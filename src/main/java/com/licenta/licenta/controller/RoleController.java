package com.licenta.licenta.controller;

import com.licenta.licenta.dto.RoleDto;
import com.licenta.licenta.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @PostMapping("${API-PATH}/tip/getAll")
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }
}
