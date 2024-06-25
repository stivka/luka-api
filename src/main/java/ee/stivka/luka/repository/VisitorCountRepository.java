package ee.stivka.luka.repository;

import ee.stivka.luka.model.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {
}
