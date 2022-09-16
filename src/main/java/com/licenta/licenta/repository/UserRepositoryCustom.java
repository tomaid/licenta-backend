package com.licenta.licenta.repository;

import com.licenta.licenta.dto.AuthDto;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<AuthDto> getByUserWithPassAndRole(String user);
}
