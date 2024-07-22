package ee.stivka.luka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.stivka.luka.model.GuestbookEntry;
import ee.stivka.luka.repository.GuestbookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DatabaseSeeder {

    private final GuestbookRepository guestbookRepository;

    @PostConstruct
    public void init() {
        guestbookRepository.save(new GuestbookEntry("Charlie", "It's Charlie baby"));
    }
}
