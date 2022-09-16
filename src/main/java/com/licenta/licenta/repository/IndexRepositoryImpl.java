package com.licenta.licenta.repository;

import com.licenta.licenta.model.Index;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class IndexRepositoryImpl implements IndexRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Index> getIndexByUserAndApartamentAndServiciuAndContor(Long userId, Long apartamentId, Long serviciuId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.id, i.contor, i.valoare_index, i.data_citire, i.autocitit) " +
                "from Index i " +
                "inner join i.contor contor " +
                "inner join contor.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.apartamente apartamente " +
                "inner join asociatie.admin admins " +
                "where contor.id=:contorId AND serviciu.id=:serviciuId AND contor.apartament.id = :apartamentId " +
                "AND :apartamentId IN (apartamente) AND ((:userId IN (admins)) OR (apartamente.user.id=:userId)) ORDER BY i.data_citire DESC, apartamente.numar_apartament ASC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("serviciuId", serviciuId)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .getResultList();
    }
    @Override
    public List<Index> getIndexByUserLocatarAndApartamentAndServiciuAndContor(Long userId, Long apartamentId, Long serviciuId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.id, i.contor, i.valoare_index, i.data_citire, i.autocitit) " +
                "from Index i " +
                "inner join i.contor contor " +
                "inner join contor.serviciu serviciu" +
                "inner join contor.apartament apartament " +
                "inner join apartament.user user " +
                "where i.contor.id=:contorId AND contor.serviciu.id=:serviciuId AND contor.apartament.id = :apartamentId " +
                "AND apartament.user.id =:userId ORDER BY i.data_citire DESC, apartament.numar_apartament ASC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("serviciuId", serviciuId)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Index> getIndexByUserAndApartamentAndServiciuAndContor(Long userId, Long serviciuId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.id, i.contor, i.valoare_index, i.data_citire, i.autocitit) " +
                "from Index i " +
                "inner join i.contor contor " +
                "inner join contor.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where contor.id=:contorId AND serviciu.id=:serviciuId " +
                "AND :userId IN (admins) ORDER BY i.data_citire DESC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("serviciuId", serviciuId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Index> getSingleIndexByUserAndApartamentAndServiciuAndContor(Long userId, Long apartamentId, Long serviciuId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.id, i.contor, i.valoare_index, i.data_citire, i.autocitit) " +
                "from Index i " +
                "inner join i.contor contor " +
                "inner join contor.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join contor.apartament apartament " +
                "inner join asociatie.admin admins " +
                "where contor.id=:contorId AND contor.serviciu.id=:serviciuId AND contor.apartament.id = :apartamentId " +
                "AND ((:userId IN (admins)) OR (apartament.user.id=:userId))  AND year(i.data_citire) = year(:currentDate) AND month(i.data_citire)=month(:currentDate) ORDER BY i.data_citire DESC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("serviciuId", serviciuId)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .setParameter("currentDate", new Date(), TemporalType.DATE)
                .getResultStream().findFirst();
    }
    @Override
    public Optional<Index> getSingleIndexByUserAndApartamentAndServiciuAndContorAndDate(Long userId, Long apartamentId, Long serviciuId, Long contorId, Date data) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.id, i.contor, i.valoare_index, i.data_citire, i.autocitit) " +
                "from Index i " +
                "inner join i.contor contor " +
                "inner join contor.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join contor.apartament apartament " +
                "inner join asociatie.admin admins " +
                "where contor.id=:contorId AND contor.serviciu.id=:serviciuId AND contor.apartament.id = :apartamentId " +
                "AND ((:userId IN (admins)) OR (apartament.user.id=:userId))  AND year(i.data_citire) = year(:currentDate) AND month(i.data_citire)=month(:currentDate) ORDER BY i.data_citire DESC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("serviciuId", serviciuId)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .setParameter("currentDate", data, TemporalType.DATE)
                .getResultStream().findFirst();
    }

    @Override
    public Optional<Index> getGeneralIndexByUserAndAndServiciuAndContor(Long userId, Long serviciuId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.id, i.contor, i.valoare_index, i.data_citire, i.autocitit) " +
                "from Index i " +
                "inner join i.contor contor " +
                "inner join contor.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where contor.id=:contorId AND contor.contor_general=true AND contor.serviciu.id=:serviciuId " +
                "AND (:userId IN (admins))  AND year(i.data_citire) = year(:currentDate) AND month(i.data_citire)=month(:currentDate) ORDER BY i.data_citire DESC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("serviciuId", serviciuId)
                .setParameter("userId", userId)
                .setParameter("currentDate", new Date(), TemporalType.DATE)
                .getResultStream().findFirst();
    }

    @Override
    public Optional<Index> getIndexbyContorAndDate(Long contorId, Date data) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.valoare_index) " +
                "from Index i " +
                " where i.contor.id=:contorId " +
                "AND year(i.data_citire) = year(:currentDate) AND month(i.data_citire)=month(:currentDate) ORDER BY i.data_citire DESC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("currentDate", data, TemporalType.DATE)
                .getResultStream().findFirst();
    }
    @Override
    public Optional<Index> getIndexbyContorAndDateAutocitit(Long contorId, Date data) {
        String query = "select new com.licenta.licenta.model.Index " +
                "(i.valoare_index, i.autocitit) " +
                "from Index i " +
                " where i.contor.id=:contorId " +
                "AND year(i.data_citire) = year(:currentDate) AND month(i.data_citire)=month(:currentDate) ORDER BY i.data_citire DESC";
        return entityManager.createQuery(query, Index.class)
                .setParameter("contorId", contorId)
                .setParameter("currentDate", data, TemporalType.DATE)
                .getResultStream().findFirst();
    }

}
