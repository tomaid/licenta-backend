package com.licenta.licenta.repository;

import com.licenta.licenta.model.Contor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ContorRepositoryImpl implements ContorRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Contor> getContoareByAsociatieAndServiciuAndAdmin(Long asocId, Long servId, Long userId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu, c.apartament) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "left join c.apartament apartament " +
                "where serviciu.id=:servId AND asociatie.id = :asocId AND :userId IN (admins) ORDER BY c.apartament.numar_apartament ASC, c.nume_contor ASC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("servId", servId)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public boolean existsContorGeneralByAsociatieAndServiciuAndAdmin(Long asocId, Long servId, Long userId) {
        String query = "select COUNT(c) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "left join c.apartament apartament " +
                "where serviciu.id=:servId AND asociatie.id = :asocId AND :userId IN (admins) AND c.contor_general IS TRUE";
         Long count = (Long) entityManager.createQuery(query)
                .setParameter("servId", servId)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .getSingleResult();
        return count > 0;
    }
    @Override
    public Optional<Contor> getContoareByAsociatieAndServiciuAndAdminAndContor(Long asocId, Long servId, Long userId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu, c.apartament) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "left join c.apartament apartament " +
                "where c.id= :contorId AND c.serviciu.id=:servId AND asociatie.id = :asocId AND :userId IN (admins) ORDER BY c.apartament.numar_apartament ASC, c.nume_contor ASC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("servId", servId)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .setParameter("contorId", contorId)
                .getResultStream().findFirst();
    }
    public Optional<Contor> getContoareByAsociatieAndServiciuAndLocatarAndContor(Long apartamentId, Long servId, Long userId, Long contorId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu, c.apartament) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "left join c.apartament apartament " +
                "inner join apartament.user user " +
                "where c.id= :contorId AND c.serviciu.id=:servId AND apartament.id = :apartamentId AND apartament.user.id =:userId ORDER BY c.apartament.numar_apartament ASC, c.nume_contor ASC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("servId", servId)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .setParameter("contorId", contorId)
                .getResultStream().findFirst();
    }

    @Override
    public List<Contor> getContoareByAsociatieAndApartament(Long userId, Long asocId, Long apartamentId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu, c.apartament) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "left join c.apartament apartament " +
                "where apartament.id=:apartamentId AND asociatie.id = :asocId AND ((:userId IN (admins)) OR (apartament.user.id=:userId)) ORDER BY c.nume_contor ASC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("userId", userId)
                .setParameter("asocId", asocId)
                .setParameter("apartamentId", apartamentId)
                .getResultList();
    }

    @Override
    public List<Contor> getContoareByLocatarAndApartament(Long userId, Long apartamentId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu, c.apartament) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "left join c.apartament apartament " +
                "left join apartament.user user " +
                "where apartament.id=:apartamentId AND apartament.user.id = :userId ORDER BY c.nume_contor ASC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("userId", userId)
                .setParameter("apartamentId", apartamentId)
                .getResultList();
    }

    @Override
    public Optional<Contor> getContoareByAsociatieAndServiciu(Long userId, Long asocId, Long serviciuId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where c.contor_general=true AND c.serviciu.id=:serviciuId AND asociatie.id = :asocId AND :userId IN (admins) ORDER BY c.contor_general DESC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("serviciuId", serviciuId)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .getResultStream().findFirst();
    }

    @Override
    public List<Contor> getContoareByAsociatieAndApartamentAndService(Long userId, Long asocId, Long apartamentId, Long serviceId) {
        String query = "select new com.licenta.licenta.model.Contor " +
                "(c.id, c.nume_contor, c.contor_general, c.serviciu, c.apartament) " +
                "from Contor c " +
                "inner join c.serviciu serviciu " +
                "inner join serviciu.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "left join c.apartament apartament " +
                "where c.serviciu.id=:serviciuId AND c.apartament.id=:apartamentId AND serviciu.asociatie.id = :asocId AND ((:userId IN (admins)) OR (apartament.user.id=:userId)) ORDER BY c.nume_contor ASC";
        return entityManager.createQuery(query, Contor.class)
                .setParameter("userId", userId)
                .setParameter("asocId", asocId)
                .setParameter("apartamentId", apartamentId)
                .setParameter("serviciuId", serviceId)
                .getResultList();
    }
}
