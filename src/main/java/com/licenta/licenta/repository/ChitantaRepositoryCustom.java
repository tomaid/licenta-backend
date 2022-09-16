package com.licenta.licenta.repository;

import com.licenta.licenta.model.Chitanta;

import java.util.List;

public interface ChitantaRepositoryCustom {
    List<Chitanta> findAllByPlataIntretinereId(Long plataIntretinereId);
}
