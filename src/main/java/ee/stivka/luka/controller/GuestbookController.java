package ee.stivka.luka.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import ee.stivka.luka.service.DiscordService;
import ee.stivka.luka.sse.SseHub;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guestbook")
public class GuestbookController {

    private final GuestbookRepository guestbookRepository;
    private final DiscordService discordService;
    private final SseHub sseHub;
    private final List<String> swearWords = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadSwearWords();
    }

    @PostMapping()
    public GuestbookEntry createEntry(@RequestBody GuestbookEntry entry) {
        if (entry.getMessage().isBlank() || entry.getName().isBlank()) {
            throw new IllegalArgumentException("Name and message are required");
        }

        if (containsSwearWord(entry.getMessage())) {
            discordService.sendSweareWordAlert(entry.getMessage());
        }

        // Ensure the createdAt field is provided by the frontend only
        if (entry.getSubmittedAt() == null) {
            throw new IllegalArgumentException("Creation time is required");
        }

        GuestbookEntry saved = guestbookRepository.save(entry);
        // Broadcast the new entry so other clients can update in real-time.
        sseHub.broadcast("guestbookEntry", saved);
        return saved;
    }

    @GetMapping
    public Page<GuestbookEntry> getAllEntries(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", required = false) Integer size,
        @RequestParam(name = "limit", required = false) Integer limit) {
        int resolvedSize = 5;
        if (limit != null) resolvedSize = limit;
        else if (size != null) resolvedSize = size;

        Pageable pageable = PageRequest.of(page, resolvedSize, Sort.Direction.DESC, "createdAt");
        return guestbookRepository.findAll(pageable);
    }

    private void loadSwearWords() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("swearWords.txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            reader.lines()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .forEach(swearWords::add);
        } catch (IOException e) {
            System.err.println("Error loading swear words: " + e.getMessage());
        }
    }

    private boolean containsSwearWord(String message) {
        String lowerCaseMessage = message.toLowerCase();
        for (String swear : swearWords) {
            if (lowerCaseMessage.contains(swear.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
