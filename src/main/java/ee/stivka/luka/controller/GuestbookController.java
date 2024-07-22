package ee.stivka.luka.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.stivka.luka.model.GuestbookEntry;
import ee.stivka.luka.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guestbook")
public class GuestbookController {

    private final GuestbookRepository guestbookRepository;

    @PostMapping()
    public GuestbookEntry createEntry(@RequestBody GuestbookEntry entry) {
        return guestbookRepository.save(entry);
    }

    @GetMapping
    public Page<GuestbookEntry> getAllEntries(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "date");
        return guestbookRepository.findAll(pageable);
    }
}
