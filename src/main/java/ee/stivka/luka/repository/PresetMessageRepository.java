package ee.stivka.luka.repository;

import ee.stivka.luka.model.PresetMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresetMessageRepository extends JpaRepository<PresetMessage, Long> {
}
