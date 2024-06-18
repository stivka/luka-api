package ee.stivka.luka.service;

import ee.stivka.luka.model.GuestbookEntry;
import ee.stivka.luka.repository.GuestbookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSeeder {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @PostConstruct
    public void init() {
        guestbookRepository.save(new GuestbookEntry("Alice", "Hello there"));
        guestbookRepository.save(new GuestbookEntry("Bob", "Hi, I'm Bob and I love Spring Boot!"));
    }
}
