package com.licenta.licenta.repository;

import com.licenta.licenta.model.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long>, IndexRepositoryCustom {
    Optional<Index> findById(Long id);
}
