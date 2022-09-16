package com.licenta.licenta.repository;

import com.licenta.licenta.dto.AuthDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<AuthDto> getByUserWithPassAndRole(String user) {
        String query = "select new com.licenta.licenta.dto.AuthDto" +
                " (u.user, u.pass, u.role.nume) " +
                " from User u " +
                " where u.user = :user";
        AuthDto authDto =
                entityManager.createQuery(query, AuthDto.class)
                        .setParameter("user", user)
                        .getSingleResult();
        return Optional.of(authDto);
    }
}
