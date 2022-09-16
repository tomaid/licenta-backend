package com.licenta.licenta.service;

import com.ia.GA;
import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.dto.AlgoGeneticProduseArrayDto;
import com.licenta.licenta.dto.AlgoGeneticReturnareListe;
import com.licenta.licenta.model.User;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AlgoritmGeneticService {

    @Autowired
    UserRepository userRepository;
    public List<AlgoGeneticReturnareListe> runAlgoGenetic(OAuth2Authentication auth2Authentication, AlgoGeneticProduseArrayDto algoGeneticProduseArrayDto) throws ResponseStatusException {
        UserPrinciple userPrinciple = (UserPrinciple) auth2Authentication.getPrincipal();
        Optional<User> user = userRepository.findByUser(userPrinciple.getUsername());
        if(user.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Userul nu exista");
        GA ga = new GA();
        ga.setMarimePopulatie(100);
        ga.setRataIncrucisare(0.75);
        ga.setRataMutatie(0.07);
        ga.setPret(algoGeneticProduseArrayDto.getPret());
        ga.setProduseDtoList(algoGeneticProduseArrayDto.getProdus());
        return  ga.runAlgoritm();
    }
}
