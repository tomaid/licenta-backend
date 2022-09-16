package com.licenta.licenta.repository;

import com.licenta.licenta.model.Apartament;
import com.licenta.licenta.model.NumarPersoane;
import com.licenta.licenta.model.SuprafataTotala;

import java.util.List;
import java.util.Optional;

public interface ApartamentRepositoryCustom {
    List<Apartament> getApartamenteByUserAndAsociatie(Long idUser, Long idAsociatie);
    Optional<NumarPersoane> getNumarPersoaneByUserAndAsociatie(Long idUser, Long idAsociatie);
    Optional<SuprafataTotala> getSuprafataTotalaByUserAndAsociatie(Long idUser, Long idAsociatie);
    List<Apartament> getApartamenteByUser(Long idUser);
    Optional<Apartament> getApartamentByUserAndId(Long idUser, Long apartamentId);
    List<Apartament> getApartamenteByAsociatie(Long idAsociatie);
    Optional<SuprafataTotala> getSuprafataTotalaByAsociatie(Long idAsociatie);

}
