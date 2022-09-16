package com.licenta.licenta.repository;

import com.licenta.licenta.model.Chitanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChitantaRepository extends JpaRepository<Chitanta, Long>, ChitantaRepositoryCustom{
}
