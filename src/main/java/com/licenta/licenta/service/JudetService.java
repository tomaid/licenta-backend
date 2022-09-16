package com.licenta.licenta.service;
import com.licenta.licenta.dto.JudetDto;
import com.licenta.licenta.model.Judet;
import com.licenta.licenta.repository.JudetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class JudetService {
    @Autowired
    private JudetRepository judetRepository;

    public List<JudetDto> getAll(OAuth2Authentication auth2Authentication){
        if(auth2Authentication == null){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nu sunteti autentificat");
        }
        List<Judet> judete = judetRepository.findAll();
        List<JudetDto> judetDtoList= new ArrayList<>();
        for(Judet judet: judete){
            JudetDto judetDto = new JudetDto(judet.getId(), judet.getNume());
            judetDtoList.add(judetDto);
        }
        return  judetDtoList;
    }

}
