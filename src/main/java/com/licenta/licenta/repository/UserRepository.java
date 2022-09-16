package com.licenta.licenta.repository;
import com.licenta.licenta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
   Optional<User> findByUser(String user);
}
