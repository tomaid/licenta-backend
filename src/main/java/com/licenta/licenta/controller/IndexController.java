package com.licenta.licenta.controller;

import com.licenta.licenta.dto.ContorIndexDto;
import com.licenta.licenta.dto.ContorIndexExtendedDto;
import com.licenta.licenta.dto.IndexDto;
import com.licenta.licenta.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IndexController {
    @Autowired
    public IndexService indexService;
    @PostMapping("${API-PATH}/asociatie/{asocId}/apartament/{apartamentId}/serviciu/{servId}/contor/{contorId}/index")
    public ResponseEntity<Object> introducereIndex(OAuth2Authentication auth2Authentication, @RequestBody IndexDto indexDto, @PathVariable Long asocId, @PathVariable Long apartamentId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.introducereIndex(auth2Authentication, indexDto, apartamentId, asocId, servId, contorId);
    }
    @PostMapping("${API-PATH}/apartament/{apartamentId}/serviciu/{servId}/contor/{contorId}/index")
    public ResponseEntity<Object> introducereIndexLocatar(OAuth2Authentication auth2Authentication, @RequestBody IndexDto indexDto, @PathVariable Long apartamentId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.introducereIndexLocatar(auth2Authentication, indexDto, apartamentId, servId, contorId);
    }
    @PostMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}/contor/{contorId}/general")
    public ResponseEntity<Object> introducereIndexGeneral(OAuth2Authentication auth2Authentication, @RequestBody IndexDto indexDto, @PathVariable Long asocId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.introducereIndexGeneral(auth2Authentication, indexDto, asocId, servId, contorId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/apartament/{apartamentId}/serviciu/{servId}/contor/{contorId}/index")
    public List<IndexDto> getIndexAll(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long apartamentId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.getAll(auth2Authentication, apartamentId, asocId, servId, contorId);
    }
    @GetMapping("${API-PATH}/apartament/{apartamentId}/serviciu/{servId}/contor/{contorId}/index")
    public List<IndexDto> getIndexAllLocatar(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.getIndexAllLocatar(auth2Authentication, apartamentId, servId, contorId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/serviciu/{servId}/contor/{contorId}/index")
    public List<IndexDto> getIndexAllNoApt(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.getIndexAllNoApt(auth2Authentication, asocId, servId, contorId);
    }
    @GetMapping("${API-PATH}/apartament/{apartamentId}/serviciu/{servId}/contor/{contorId}/indexAll")
    public List<IndexDto> getIndexAllApt(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId, @PathVariable Long servId, @PathVariable Long contorId){
        return indexService.getIndexAllApt(auth2Authentication, apartamentId, servId, contorId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/apartament/{apartamentId}/indecsi")
    public List<ContorIndexDto> getIndexNoService(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long apartamentId){
        return indexService.getIndexNoService(auth2Authentication, apartamentId, asocId);
    }
    @GetMapping("${API-PATH}//apartament/{apartamentId}/indecsi")
    public List<ContorIndexExtendedDto> getIndexNoServiceLocatar(OAuth2Authentication auth2Authentication, @PathVariable Long apartamentId){
        return indexService.getIndexNoServiceLocatar(auth2Authentication, apartamentId);
    }
    @GetMapping("${API-PATH}/asociatie/{asocId}/serviciu/{serviciuId}/contorl-general")
    public ContorIndexDto getIndexByService(OAuth2Authentication auth2Authentication, @PathVariable Long asocId, @PathVariable Long serviciuId){
        return indexService.getIndexByService(auth2Authentication, asocId, serviciuId);
    }
}
