package com.licenta.licenta.service;

import com.licenta.licenta.dto.LocalitateDto;
import com.licenta.licenta.model.Localitate;
import com.licenta.licenta.repository.LocalitateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocalitateService {
    @Autowired
    LocalitateRepository localitateRepository;

    public List<LocalitateDto> getAll() {
        List<Localitate> localitati = localitateRepository.findAll();
        List<LocalitateDto> localitateDtoList=new ArrayList<>();
        for(Localitate localitate: localitati){
            LocalitateDto localitateDto = new LocalitateDto(localitate.getId(),localitate.getNume());
            localitateDtoList.add(localitateDto);
        }
        return localitateDtoList;
    }

    public List<LocalitateDto> getAllbyJudetId(Long judetId) {
        return localitateRepository.getByLocalitateWithIdAndNume(judetId);
    }
}
