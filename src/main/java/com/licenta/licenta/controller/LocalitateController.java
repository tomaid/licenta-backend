package com.licenta.licenta.controller;

import com.licenta.licenta.dto.LocalitateDto;
import com.licenta.licenta.service.LocalitateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocalitateController {
    @Autowired
    LocalitateService localitateService;
    @PostMapping("${API-PATH}/localitati/getAll")
    public List<LocalitateDto> getAll(){
        return localitateService.getAll();
    }
    @GetMapping("${API-PATH}/localitati/dinJudet/{judet}")
    public List<LocalitateDto> getAllByJudetId(@PathVariable Long judet){
        return localitateService.getAllbyJudetId(judet);
    }
}
