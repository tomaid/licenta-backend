package com.licenta.licenta.repository;

import com.licenta.licenta.model.Apartament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartamentRepository extends JpaRepository<Apartament,Long>, ApartamentRepositoryCustom {
    Optional<Apartament> findById(Long id);
}
