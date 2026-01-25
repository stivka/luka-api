package ee.stivka.luka.service;

import ee.stivka.luka.sse.SseHub;
import ee.stivka.luka.model.VisitorCount;
import ee.stivka.luka.repository.VisitorCountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VisitorCountService {

    private final VisitorCountRepository repository;
    private final SseHub sseHub;

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
        Long newCount = count.getCount();
        
        // Broadcast the updated count via SSE
        sseHub.broadcast("visitorCount", Map.of("count", newCount));
        
        return newCount;
    }

    public Long getVisitorCount() {
        return repository.findAll().getFirst().getCount();
    }
}