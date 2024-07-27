package ee.stivka.luka.repository;

import ee.stivka.luka.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
}
