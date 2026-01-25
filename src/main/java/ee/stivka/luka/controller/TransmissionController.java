package ee.stivka.luka.controller;

import ee.stivka.luka.sse.SseHub;
import ee.stivka.luka.service.RandomTransmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transmission")
@Tag(name = "Transmissions", description = "Endpoints for manual transmissions and tuning")
public class TransmissionController {
    private final SseHub hub;
    private final RandomTransmissionService randomSvc;

    @PostMapping("/interval")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Set transmission interval", description = "Sets the min and max interval for random transmissions.")
    public void setInterval(@RequestParam long minMs, @RequestParam long maxMs) {
        randomSvc.updateInterval(minMs, maxMs);
    }

    @PostMapping("/transmit")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Manual transmission", description = "Broadcasts a manual transmission message to all SSE subscribers.")
    public void transmit(@RequestParam String message) {
        hub.broadcast("notification", Map.of(
                "type", "TEST",
                "message", message,
                "ts", Instant.now().toString(),
                "tx", randomSvc.getTransmitterId()
        ));
    }
}
