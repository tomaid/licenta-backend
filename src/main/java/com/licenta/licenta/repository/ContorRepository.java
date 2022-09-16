package com.licenta.licenta.repository;

import com.licenta.licenta.model.Contor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContorRepository extends JpaRepository<Contor,Long>, ContorRepositoryCustom {
    Optional<Contor> findById(Long id);
}
