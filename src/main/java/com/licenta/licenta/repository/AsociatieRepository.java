package com.licenta.licenta.repository;

import com.licenta.licenta.model.Asociatie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsociatieRepository extends JpaRepository<Asociatie, Long> {
    Optional<Asociatie> findByNume(String nume);
    Optional<Asociatie> findById(Long id);
}
