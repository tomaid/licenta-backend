package com.licenta.licenta.repository;

import com.licenta.licenta.dto.LocalitateDto;

import java.util.List;

public interface LocalitateRepositoryCustom {
    List<LocalitateDto> getByLocalitateWithIdAndNume(Long id);
}
