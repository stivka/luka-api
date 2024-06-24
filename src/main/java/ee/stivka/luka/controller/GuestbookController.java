package ee.stivka.luka.controller;

import ee.stivka.luka.model.GuestbookEntry;
import ee.stivka.luka.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guestbook")
public class GuestbookController {

    private final GuestbookRepository guestbookRepository;

    @PostMapping()
    public GuestbookEntry createEntry(@RequestBody GuestbookEntry entry) {
        entry.setDate(entry.getDate());
        return guestbookRepository.save(entry);
    }

    @GetMapping
    public Page<GuestbookEntry> getAllEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return guestbookRepository.findAll(pageable);
    }
}
