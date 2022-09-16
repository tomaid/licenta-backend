package com.licenta.licenta.repository;

import com.licenta.licenta.model.PlataIntretinere;
import com.licenta.licenta.model.PlataIntretinereDetalii;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PlataIntretinereRepositoryImpl implements PlataIntretinereRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<PlataIntretinere> getByAsociatieAndUser(Long asocId, Long userId) {
            String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                    "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                    "from PlataIntretinere pi " +
                    "inner join pi.apartament apartament " +
                    "inner join apartament.asociatie asociatie " +
                    "inner join asociatie.admin admins " +
                    "where apartament.asociatie.id=:asocId " +
                    "AND :userId IN (admins) ORDER BY pi.data_intocmire DESC";
            return entityManager.createQuery(query, PlataIntretinere.class)
                    .setParameter("asocId", asocId)
                    .setParameter("userId", userId)
                    .getResultList();
        }

    @Override
    public List<PlataIntretinere> getByAsociatieAndUserAndDate(Long asocId, Long userId, int luna, int an) {
        String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                "from PlataIntretinere pi " +
                "inner join pi.apartament apartament " +
                "inner join apartament.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where apartament.asociatie.id=:asocId " +
                "AND :userId IN (admins) AND  pi.an = :an AND pi.luna=:luna ORDER BY pi.data_intocmire DESC";
        return entityManager.createQuery(query, PlataIntretinere.class)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .setParameter("luna", luna)
                .setParameter("an", an)
                .getResultList();
    }
    public List<PlataIntretinere> getByAsociatieAndUserAndDateAndApartment(Long asocId, Long userId, int luna, int an, Long apartmentId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                "from PlataIntretinere pi " +
                "inner join pi.apartament apartament " +
                "inner join apartament.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where apartament.asociatie.id=:asocId " +
                "AND :userId IN (admins) AND  pi.an = :an AND pi.luna=:luna AND pi.apartament.id = :apartamentId ORDER BY pi.data_intocmire DESC";
        return entityManager.createQuery(query, PlataIntretinere.class)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .setParameter("apartamentId", apartmentId)
                .setParameter("luna", luna)
                .setParameter("an", an)
                .getResultList();
    }
    public Optional<PlataIntretinere> getByIdAndUserAndAsociatie(Long asocId, Long userId, Long facturaId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                "from PlataIntretinere pi " +
                "inner join pi.apartament apartament " +
                "inner join apartament.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where pi.id= :facturaId AND apartament.asociatie.id=:asocId " +
                "AND :userId IN (admins) ORDER BY pi.data_intocmire DESC";
        return entityManager.createQuery(query, PlataIntretinere.class)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .setParameter("facturaId", facturaId)
                .getResultStream().findFirst();
    }

    @Override
    public List<PlataIntretinereDetalii> getByIdAndUserAndAsociatieAndFactura(Long asocId, Long userId, Long facturaId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinereDetalii " +
                "(pi.id, pi.suma, pi.textDetalii, pi.consum) " +
                "from PlataIntretinereDetalii pi " +
                "inner join pi.plataIntretinere plataIntretinere " +
                "inner join plataIntretinere.apartament apartament " +
                "inner join apartament.asociatie asociatie " +
                "inner join asociatie.admin admins " +
                "where apartament.asociatie.id=:asocId " +
                "AND :userId IN (admins) AND pi.plataIntretinere.id=:facturaId ORDER BY pi.textDetalii ASC";
        return entityManager.createQuery(query, PlataIntretinereDetalii.class)
                .setParameter("asocId", asocId)
                .setParameter("userId", userId)
                .setParameter("facturaId", facturaId)
                .getResultList();
    }

    @Override
    public List<PlataIntretinereDetalii> getByIdAndAsociatieAndFactura(Long asocId,  Long facturaId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinereDetalii " +
                "(pi.id, pi.suma, pi.textDetalii, pi.consum) " +
                "from PlataIntretinereDetalii pi " +
                "inner join pi.plataIntretinere plataIntretinere " +
                "inner join plataIntretinere.apartament apartament " +
                "inner join apartament.asociatie asociatie " +
                "where apartament.asociatie.id=:asocId " +
                "AND pi.plataIntretinere.id=:facturaId ORDER BY pi.textDetalii ASC";
        return entityManager.createQuery(query, PlataIntretinereDetalii.class)
                .setParameter("asocId", asocId)
                .setParameter("facturaId", facturaId)
                .getResultList();
    }

    @Override
    public Optional<PlataIntretinere> getByApartamentAndLunaAndAn(Long apartamentId, int luna, int an) {
        String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                "from PlataIntretinere pi " +
                "where  pi.an = :an AND pi.luna=:luna AND pi.apartament.id = :apartamentId  ORDER BY pi.data_intocmire DESC";
        return entityManager.createQuery(query, PlataIntretinere.class)
                .setParameter("an", an)
                .setParameter("luna", luna)
                .setParameter("apartamentId", apartamentId)
                .getResultStream().findFirst();
    }

    @Override
    public List<PlataIntretinere> getByApartamentAndUser(Long aptId, Long userId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                "from PlataIntretinere pi " +
                "inner join pi.apartament apartament " +
                "inner join apartament.user user " +
                "where apartament.id=:aptId AND " +
                "apartament.user.id =:userId " +
                "ORDER BY pi.data_intocmire DESC";
        return entityManager.createQuery(query, PlataIntretinere.class)
                .setParameter("aptId", aptId)
                .setParameter("userId", userId)
                .getResultList();

    }
    public Optional<PlataIntretinere> getByIdAndUserAndApartament(Long apartamentId, Long userId, Long facturaId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinere " +
                "(pi.id, pi.apartament, pi.user, pi.an, pi.luna, pi.data_intocmire, pi.suma, pi.achitatComplet) " +
                "from PlataIntretinere pi " +
                "inner join pi.apartament apartament " +
                "inner join apartament.user user " +
                "where pi.id= :facturaId AND pi.apartament.id=:apartamentId " +
                "AND apartament.user.id =:userId ORDER BY pi.data_intocmire DESC";
        return entityManager.createQuery(query, PlataIntretinere.class)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .setParameter("facturaId", facturaId)
                .getResultStream().findFirst();
    }
    @Override
    public List<PlataIntretinereDetalii> getByIdAndUserAndApartamentAndFactura(Long apartamentId, Long userId, Long facturaId) {
        String query = "select new com.licenta.licenta.model.PlataIntretinereDetalii " +
                "(pi.id, pi.suma, pi.textDetalii, pi.consum) " +
                "from PlataIntretinereDetalii pi " +
                "inner join pi.plataIntretinere plataIntretinere " +
                "inner join plataIntretinere.apartament apartament " +
                "inner join apartament.user user " +
                "where plataIntretinere.apartament.id=:apartamentId AND apartament.user.id =:userId " +
                "AND pi.plataIntretinere.id=:facturaId ORDER BY pi.textDetalii ASC";
        return entityManager.createQuery(query, PlataIntretinereDetalii.class)
                .setParameter("apartamentId", apartamentId)
                .setParameter("userId", userId)
                .setParameter("facturaId", facturaId)
                .getResultList();
    }

}
