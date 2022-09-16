package com.licenta.licenta.repository;

import com.licenta.licenta.model.PlataIntretinere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlataIntretinereRepository extends JpaRepository<PlataIntretinere, Long>, PlataIntretinereRepositoryCustom {
}
