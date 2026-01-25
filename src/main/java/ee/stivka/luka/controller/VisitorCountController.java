package ee.stivka.luka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.stivka.luka.service.VisitorCountService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visitor-count")
@Tag(name = "Visitor Count", description = "Endpoints for managing the visitor counter")
public class VisitorCountController {
    private final VisitorCountService service;

    @GetMapping
    @Operation(summary = "Get count", description = "Gets the current visitor count.")
    public Long getCount() {
        return service.getVisitorCount();
    }
}
