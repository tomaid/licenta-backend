package com.licenta.licenta.repository;

import com.licenta.licenta.model.Contor;

import java.util.List;
import java.util.Optional;

public interface ContorRepositoryCustom {
    List<Contor> getContoareByAsociatieAndServiciuAndAdmin(Long asocId, Long servId, Long userId);
    boolean existsContorGeneralByAsociatieAndServiciuAndAdmin(Long asocId, Long servId, Long userId);
    Optional<Contor> getContoareByAsociatieAndServiciuAndAdminAndContor(Long asocId, Long servId, Long userId, Long contorId);
    Optional<Contor> getContoareByAsociatieAndServiciuAndLocatarAndContor(Long apartamentId, Long servId, Long userId, Long contorId);
    List<Contor> getContoareByAsociatieAndApartament(Long userId, Long asocId, Long apartamentId);
    List<Contor> getContoareByLocatarAndApartament(Long userId, Long apartamentId);
    Optional<Contor> getContoareByAsociatieAndServiciu(Long userId, Long asocId, Long serviciuId);
    List<Contor> getContoareByAsociatieAndApartamentAndService(Long userId, Long asocId, Long apartamentId, Long serviceId);
}
