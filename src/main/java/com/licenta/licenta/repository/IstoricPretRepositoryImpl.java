package com.licenta.licenta.repository;

import com.licenta.licenta.model.IstoricPret;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class IstoricPretRepositoryImpl implements IstoricPretRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<IstoricPret> getByDateAndServiciu(Long serviciuId, Timestamp data_cautarii) {
        String query = "select new com.licenta.licenta.model.IstoricPret " +
                "(ip.id, ip.serviciu, ip.pret, ip.data_pret) " +
                "from IstoricPret ip " +
                "inner join ip.serviciu serviciu " +
                "where ip.serviciu.id=:serviciuId AND ip.data_pret <= :data_cautarii ORDER BY ip.data_pret DESC";
        return entityManager.createQuery(query, IstoricPret.class)
                .setParameter("serviciuId", serviciuId)
                .setParameter("data_cautarii", data_cautarii, TemporalType.TIMESTAMP)
                .getResultStream().findFirst();
    }
}
