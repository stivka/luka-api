package ee.stivka.luka.controller;

import ee.stivka.luka.model.PresetMessage;
import ee.stivka.luka.repository.PresetMessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preset-messages")
@Tag(name = "Preset Messages", description = "Endpoints for managing the random transmission message pool")
public class PresetMessageController {

    private final PresetMessageRepository messageRepository;

    @GetMapping
    @Operation(summary = "Get all messages", description = "Retrieves all messages in the pool.")
    public List<PresetMessage> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add a message", description = "Adds a new message to the pool.")
    public PresetMessage addMessage(@RequestBody PresetMessage message) {
        return messageRepository.save(message);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a message", description = "Removes a message from the pool by ID.")
    public void deleteMessage(@PathVariable Long id) {
        messageRepository.deleteById(id);
    }
}
