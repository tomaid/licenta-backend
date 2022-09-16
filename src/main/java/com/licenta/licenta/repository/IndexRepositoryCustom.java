package com.licenta.licenta.repository;
import com.licenta.licenta.model.Index;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IndexRepositoryCustom {
    List<Index> getIndexByUserAndApartamentAndServiciuAndContor(Long userId, Long apartamentId, Long serviciuId, Long contorId);
    List<Index> getIndexByUserLocatarAndApartamentAndServiciuAndContor(Long userId, Long apartamentId, Long serviciuId,  Long contorId);
    List<Index> getIndexByUserAndApartamentAndServiciuAndContor(Long userId, Long serviciuId, Long contorId);
    Optional<Index> getSingleIndexByUserAndApartamentAndServiciuAndContor(Long userId, Long apartamentId, Long serviciuId, Long contorId);
    public Optional<Index> getSingleIndexByUserAndApartamentAndServiciuAndContorAndDate(Long userId, Long apartamentId, Long serviciuId, Long contorId, Date data);
    Optional<Index> getGeneralIndexByUserAndAndServiciuAndContor(Long userId, Long serviciuId, Long contorId);
    Optional<Index> getIndexbyContorAndDate(Long contorId, Date data);
    Optional<Index> getIndexbyContorAndDateAutocitit(Long contorId, Date data);
}
