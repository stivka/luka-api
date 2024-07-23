package ee.stivka.luka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.stivka.luka.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
