package ee.stivka.luka.controller;

import ee.stivka.luka.SseHub;
import ee.stivka.luka.service.RandomTransmissionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/transmissions")
public class TransmissionController {
    private final SseHub hub;
    private final RandomTransmissionService randomSvc;

    public TransmissionController(SseHub hub, RandomTransmissionService randomSvc) {
        this.hub = hub;
        this.randomSvc = randomSvc;
    }

    @GetMapping(value="/stream", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return hub.connect();
    }

    // Simple runtime tuning endpoint (needs to be a protected endpoint!)
    // Perhaps to just put it behind basic auth, since i've never tried that
    @PostMapping("/interval")
    public void setInterval(@RequestParam long minMs, @RequestParam long maxMs) {
        randomSvc.updateInterval(minMs, maxMs);
    }

    // Optional: broadcast immediately to test
    @PostMapping("/test")
    public void test(@RequestParam String message) {
        hub.broadcast("notification", Map.of("type","TEST","message",message,"ts", Instant.now().toString()));
    }
}

