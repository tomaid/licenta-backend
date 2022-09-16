package com.licenta.licenta.repository;

import com.licenta.licenta.model.Cheltuiala;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CheltuialaRepositoryImpl implements CheltuialaRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Optional<Cheltuiala> findByIdAndUserAndAsociatie(Long cheltuialaId, Long asocId, Long userId) {
        String query = "select new com.licenta.licenta.model.Cheltuiala " +
                "(c.id, c.asociatie, c.nume_cheltuiala, c.calculCheltuiala, c.data_introducere, c.suma, c.numar_factura, c.serie_factura) " +
                "from Cheltuiala c " +
                "inner join c.calculCheltuiala  calcul " +
                "inner join c.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where c.id=:cheltuialaId AND c.asociatie.id = :asocId " +
                "AND :userId IN (admins) ORDER BY c.id DESC";
        return entityManager.createQuery(query, Cheltuiala.class)
                .setParameter("cheltuialaId", cheltuialaId)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .getResultStream().findFirst();
    }

    @Override
    public List<Cheltuiala> findAllByUserAndAsociatie(Long asocId, Long userId) {
        String query = "select new com.licenta.licenta.model.Cheltuiala " +
                "(c.id, c.asociatie, c.nume_cheltuiala, c.calculCheltuiala, c.data_introducere, c.suma, c.numar_factura, c.serie_factura) " +
                "from Cheltuiala c " +
                "inner join c.calculCheltuiala  calcul " +
                "inner join c.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where c.asociatie.id = :asocId " +
                "AND :userId IN (admins) ORDER BY c.id DESC";
        return entityManager.createQuery(query, Cheltuiala.class)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Cheltuiala> findAllByUserAndAsociatieAndData(Long asocId, Long userId, Date firstDayOfMonth, Date lastDayOfMonth) {
        String query = "select new com.licenta.licenta.model.Cheltuiala " +
                "(c.id, c.asociatie, c.nume_cheltuiala, c.calculCheltuiala, c.data_introducere, c.suma, c.numar_factura, c.serie_factura) " +
                "from Cheltuiala c " +
                "inner join c.calculCheltuiala  calcul " +
                "inner join c.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where c.asociatie.id = :asocId AND c.data_introducere>=:fday AND c.data_introducere<=:lday " +
                "AND :userId IN (admins) ORDER BY c.id DESC";
        return entityManager.createQuery(query, Cheltuiala.class)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .setParameter("fday", firstDayOfMonth, TemporalType.TIMESTAMP)
                .setParameter("lday", lastDayOfMonth, TemporalType.TIMESTAMP)
                .getResultList();
    }
    @Override
    public List<Cheltuiala> findAllByAndAsociatieAndData(Long asocId, Date firstDayOfMonth, Date lastDayOfMonth) {
        String query = "select new com.licenta.licenta.model.Cheltuiala " +
                "(c.id, c.asociatie, c.nume_cheltuiala, c.calculCheltuiala, c.data_introducere, c.suma, c.numar_factura, c.serie_factura) " +
                "from Cheltuiala c " +
                "inner join c.calculCheltuiala  calcul " +
                "inner join c.asociatie asociatie " +
                "where c.asociatie.id = :asocId AND c.data_introducere>=:fday AND c.data_introducere<=:lday " +
                "ORDER BY c.id DESC";
        return entityManager.createQuery(query, Cheltuiala.class)
                .setParameter("asocId", asocId)
                .setParameter("fday", firstDayOfMonth, TemporalType.TIMESTAMP)
                .setParameter("lday", lastDayOfMonth, TemporalType.TIMESTAMP)
                .getResultList();
    }
}
