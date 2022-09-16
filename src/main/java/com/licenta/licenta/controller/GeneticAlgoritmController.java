package com.licenta.licenta.controller;

import com.licenta.licenta.dto.AlgoGeneticProduseArrayDto;
import com.licenta.licenta.dto.AlgoGeneticReturnareListe;
import com.licenta.licenta.service.AlgoritmGeneticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeneticAlgoritmController {
    @Autowired
    AlgoritmGeneticService algoritmGeneticService;

    @PostMapping("${API-PATH}/algorim-genetic")
    public List<AlgoGeneticReturnareListe> runAlgoGenetic(OAuth2Authentication auth2Authentication, @RequestBody AlgoGeneticProduseArrayDto algoGeneticProduseArrayDto){
        return algoritmGeneticService.runAlgoGenetic(auth2Authentication, algoGeneticProduseArrayDto);
    }

}
