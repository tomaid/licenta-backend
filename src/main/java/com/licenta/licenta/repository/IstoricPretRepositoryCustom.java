package com.licenta.licenta.repository;

import com.licenta.licenta.model.IstoricPret;

import java.sql.Timestamp;
import java.util.Optional;

public interface IstoricPretRepositoryCustom {
    Optional<IstoricPret> getByDateAndServiciu(Long serviciuId, Timestamp data_cautarii);
}
