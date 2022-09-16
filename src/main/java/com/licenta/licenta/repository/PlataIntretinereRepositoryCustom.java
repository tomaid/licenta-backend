package com.licenta.licenta.repository;

import com.licenta.licenta.model.PlataIntretinere;
import com.licenta.licenta.model.PlataIntretinereDetalii;

import java.util.List;
import java.util.Optional;

public interface PlataIntretinereRepositoryCustom {
    List<PlataIntretinere> getByAsociatieAndUser(Long asocId, Long userId);
    List<PlataIntretinere> getByAsociatieAndUserAndDate(Long asocId, Long userId, int luna, int an);
    public Optional<PlataIntretinere> getByIdAndUserAndAsociatie(Long asocId, Long userId, Long facturaId);
    public List<PlataIntretinereDetalii> getByIdAndUserAndAsociatieAndFactura(Long asocId, Long userId, Long facturaId);
    public List<PlataIntretinere> getByAsociatieAndUserAndDateAndApartment(Long asocId, Long userId, int luna, int an, Long apartmentId);
    public Optional<PlataIntretinere> getByApartamentAndLunaAndAn(Long apartamentId, int luna, int an);
    public List<PlataIntretinereDetalii> getByIdAndAsociatieAndFactura(Long asocId,  Long facturaId);
    List<PlataIntretinere> getByApartamentAndUser(Long aptId, Long userId);
    Optional<PlataIntretinere> getByIdAndUserAndApartament(Long apartamentId, Long userId, Long facturaId);
    List<PlataIntretinereDetalii> getByIdAndUserAndApartamentAndFactura(Long apartamentId, Long userId, Long facturaId);
}
