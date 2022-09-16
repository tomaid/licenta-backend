package com.licenta.licenta.repository;

import com.licenta.licenta.model.Judet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JudetRepository extends JpaRepository<Judet, Long> {
    Optional<Judet> findByNume(String nume);
    Optional<Judet> findById(Long id);

}
