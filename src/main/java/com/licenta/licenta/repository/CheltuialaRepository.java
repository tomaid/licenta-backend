package com.licenta.licenta.repository;

import com.licenta.licenta.model.Cheltuiala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheltuialaRepository extends JpaRepository<Cheltuiala,Long>, CheltuialaRepositoryCustom {
}
