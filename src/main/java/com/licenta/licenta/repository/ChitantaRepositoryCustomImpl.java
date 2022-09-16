package com.licenta.licenta.repository;

import com.licenta.licenta.model.Chitanta;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ChitantaRepositoryCustomImpl implements ChitantaRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Chitanta> findAllByPlataIntretinereId(Long plataIntretinereId) {
        String query = "select new com.licenta.licenta.model.Chitanta " +
                "(ch.id, ch.serie_chitanta, ch.detalii,  ch.data_achitare, ch.suma) " +
                "from Chitanta ch " +
                "inner join ch.plataIntretinere plataIntretinere " +
                "where ch.plataIntretinere.id=:plataIntretinereId " +
                " ORDER BY ch.data_achitare DESC";
        return entityManager.createQuery(query, Chitanta.class)
                .setParameter("plataIntretinereId", plataIntretinereId)
                .getResultList();
    }
}
