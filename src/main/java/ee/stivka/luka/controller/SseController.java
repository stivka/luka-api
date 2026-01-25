package ee.stivka.luka.controller;

import ee.stivka.luka.sse.SseHub;
import ee.stivka.luka.service.VisitorCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stream")
@Tag(name = "SSE", description = "Endpoints for Server-Sent Events")
public class SseController {
    private final SseHub hub;
    private final VisitorCountService visitorCountSvc;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Subscribe to stream", description = "Initiates an SSE stream and increments the visitor count.")
    public SseEmitter stream() {
        SseEmitter emitter = hub.connect();
        
        // Increment visitor count on stream initiation
        Long newCount = visitorCountSvc.incrementVisitorCount();
        
        // Send initial data to the new subscriber
        hub.sendInitial(emitter, "visitorCount", Map.of("count", newCount));
        
        return emitter;
    }
}
