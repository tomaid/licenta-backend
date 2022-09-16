package com.licenta.licenta.repository;

import com.licenta.licenta.model.Serviciu;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiciuRepositoryImpl implements ServiciuRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Optional<Serviciu> getByNume(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Serviciu> getServiciiByUserAndAsociatie(Long idUser, Long idAsociatie) {
        String query = "select new com.licenta.licenta.model.Serviciu " +
                "(s.id, s.nume_serviciu, s.pret_serviciu, s.asociatie) " +
                " from Serviciu s " +
                " inner join s.asociatie asociatie" +
                " inner join asociatie.admin admins" +
                " where s.asociatie.id=:id AND :idUser IN (admins) ";
        List<Serviciu> servicii =
                entityManager.createQuery(query, Serviciu.class)
                        .setParameter("id",idAsociatie)
                        .setParameter("idUser",idUser)
                        .getResultList();
        return servicii;
    }

    @Override
    public Optional<Serviciu> findTopByIdDesc() {

        String query = "select new com.licenta.licenta.model.Serviciu " +
                "(s.id, s.nume_serviciu, s.pret_serviciu, s.asociatie) " +
                " from Serviciu s " +
                "ORDER BY s.id DESC";
        Optional<Serviciu> serviciu =
                entityManager.createQuery(query, Serviciu.class)
                        .getResultStream().findFirst();
        return serviciu;
    }
}
