package com.licenta.licenta.repository;

import com.licenta.licenta.model.Cheltuiala;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CheltuialaRepositoryCustom {
Optional<Cheltuiala> findByIdAndUserAndAsociatie(Long cheltuialaId, Long asocId, Long userId);
List<Cheltuiala> findAllByUserAndAsociatie(Long asocId, Long userId);
List<Cheltuiala> findAllByUserAndAsociatieAndData(Long asocId, Long userId, Date firstDayOfMonth, Date lastDayOfMonth);
List<Cheltuiala> findAllByAndAsociatieAndData(Long asocId, Date firstDayOfMonth, Date lastDayOfMonth);

}
