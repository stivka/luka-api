package ee.stivka.luka.repository;

import ee.stivka.luka.model.GuestbookEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<GuestbookEntry, Long> {
    Page<GuestbookEntry> findAll(Pageable pageable);
}
