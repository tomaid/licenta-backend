package com.licenta.licenta.repository;

import com.licenta.licenta.dto.ServiciuDto;
import com.licenta.licenta.model.Serviciu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiciuRepository extends JpaRepository<Serviciu, Long>, ServiciuRepositoryCustom {
    Optional<Serviciu> findById(Long id);
}

