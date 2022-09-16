package com.licenta.licenta.repository;

import com.licenta.licenta.model.IstoricPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IstoricPretRepository extends JpaRepository<IstoricPret,Long>,IstoricPretRepositoryCustom {
}
