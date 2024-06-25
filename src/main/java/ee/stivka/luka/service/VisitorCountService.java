package ee.stivka.luka.service;

import ee.stivka.luka.model.VisitorCount;
import ee.stivka.luka.repository.VisitorCountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorCountService {

    private final VisitorCountRepository repository;

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            VisitorCount count = new VisitorCount();
            count.setCount(0L);
            repository.save(count);
        }
    }

    public Long incrementVisitorCount() {
        VisitorCount count = repository.findAll().getFirst();
        count.setCount(count.getCount() + 1);
        repository.save(count);
        return count.getCount();
    }

    public Long getVisitorCount() {
        return repository.findAll().getFirst().getCount();
    }
}