package com.licenta.licenta.repository;

import com.licenta.licenta.model.Serviciu;

import java.util.List;
import java.util.Optional;

public interface ServiciuRepositoryCustom {
    Optional<Serviciu> getByNume(Long id);
    List<Serviciu> getServiciiByUserAndAsociatie(Long idUser, Long idAsociatie);
    Optional<Serviciu> findTopByIdDesc();

}
