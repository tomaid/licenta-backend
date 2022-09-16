package com.licenta.licenta.repository;

import com.licenta.licenta.dto.LocalitateDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LocalitateRepositoryImpl implements LocalitateRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List <LocalitateDto> getByLocalitateWithIdAndNume(Long id){
        String query = "select new com.licenta.licenta.dto.LocalitateDto " +
                "(l.id, l.nume) " +
                " from Localitate l " +
                " where l.judet.id=:id";
        List<LocalitateDto> localitateDto =
                entityManager.createQuery(query, LocalitateDto.class)
                        .setParameter("id",id)
                        .getResultList();
        // System.out.println(localitateDto);
        return localitateDto;
    }
}
