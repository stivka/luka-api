package ee.stivka.luka.controller;

import ee.stivka.luka.model.GuestbookEntry;
import ee.stivka.luka.repository.GuestbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guestbook")
public class GuestbookController {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @PostMapping
    public GuestbookEntry addEntry(@RequestBody GuestbookEntry entry) {
        return guestbookRepository.save(entry);
    }

    @GetMapping
    public List<GuestbookEntry> getAllEntries() {
        return guestbookRepository.findAll();
    }
}
