package com.licenta.licenta.repository;

import com.licenta.licenta.model.Localitate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalitateRepository extends JpaRepository<Localitate, Long>, LocalitateRepositoryCustom {
    Optional<Localitate> findByNume(String nume);
}
