package com.licenta.licenta.repository;

import com.licenta.licenta.model.Apartament;
import com.licenta.licenta.model.NumarPersoane;
import com.licenta.licenta.model.SuprafataTotala;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ApartamentRepositoryImpl implements ApartamentRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Apartament> getApartamenteByUserAndAsociatie(Long idUser, Long idAsociatie) {
        String query = "select new com.licenta.licenta.model.Apartament " +
                "(a.id, a.numar_apartament, a.suprafata_mp, a.numar_locatari, a.asociatie, a.user) " +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where a.asociatie.id=:idAsociatie AND :idUser IN (admins)";
        return entityManager.createQuery(query,Apartament.class)
                .setParameter("idUser", idUser)
                .setParameter("idAsociatie", idAsociatie)
                .getResultList();
    }
    public List<Apartament> getApartamenteByAsociatie(Long idAsociatie) {
        String query = "select new com.licenta.licenta.model.Apartament " +
                "(a.id, a.numar_apartament, a.suprafata_mp, a.numar_locatari, a.asociatie, a.user) " +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "where a.asociatie.id=:idAsociatie";
        return entityManager.createQuery(query,Apartament.class)
                .setParameter("idAsociatie", idAsociatie)
                .getResultList();
    }
    @Override
    public List<Apartament> getApartamenteByUser(Long idUser) {
        String query = "select new com.licenta.licenta.model.Apartament " +
                "(a.id, a.numar_apartament, a.suprafata_mp, a.numar_locatari, a.asociatie, a.user) " +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where a.user.id=:idUser order by a.asociatie.strada, a.numar_apartament";
        return entityManager.createQuery(query,Apartament.class)
                .setParameter("idUser", idUser)
                .getResultList();
    }
    @Override
    public Optional<Apartament> getApartamentByUserAndId(Long idUser, Long apartamentId) {
        String query = "select new com.licenta.licenta.model.Apartament " +
                "(a.id, a.numar_apartament, a.suprafata_mp, a.numar_locatari, a.asociatie, a.user) " +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where a.id=:apartamentId AND a.user.id=:idUser order by a.id desc";
        return entityManager.createQuery(query,Apartament.class)
                .setParameter("idUser", idUser)
                .setParameter("apartamentId", apartamentId)
                .getResultStream().findFirst();
    }

    @Override
    public Optional<NumarPersoane> getNumarPersoaneByUserAndAsociatie(Long idUser, Long idAsociatie) {
        String query = "select new com.licenta.licenta.model.NumarPersoane (SUM(a.numar_locatari))" +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where a.asociatie.id=:idAsociatie AND :idUser IN (admins) group by a.asociatie.id";
        return entityManager.createQuery(query, NumarPersoane.class)
                .setParameter("idUser", idUser)
                .setParameter("idAsociatie", idAsociatie)
                .getResultStream().findFirst();
    }

    @Override
    public Optional<SuprafataTotala> getSuprafataTotalaByUserAndAsociatie(Long idUser, Long idAsociatie) {
        String query = "select new com.licenta.licenta.model.SuprafataTotala (SUM(a.suprafata_mp))" +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where a.asociatie.id=:idAsociatie AND :idUser IN (admins) group by a.asociatie.id";
        return entityManager.createQuery(query, SuprafataTotala.class)
                .setParameter("idUser", idUser)
                .setParameter("idAsociatie", idAsociatie)
                .getResultStream().findFirst();
    }

    @Override
    public Optional<SuprafataTotala> getSuprafataTotalaByAsociatie(Long idAsociatie) {
        String query = "select new com.licenta.licenta.model.SuprafataTotala (SUM(a.suprafata_mp))" +
                "from Apartament a " +
                "inner join a.asociatie asociatie " +
                "where a.asociatie.id=:idAsociatie group by a.asociatie.id";
        return entityManager.createQuery(query, SuprafataTotala.class)
                .setParameter("idAsociatie", idAsociatie)
                .getResultStream().findFirst();
    }
}
