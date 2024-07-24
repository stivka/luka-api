package ee.stivka.luka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.stivka.luka.service.VisitorCountService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visitor-count")
public class VisitorCountController {
    private final VisitorCountService service;

    @GetMapping
    public Long getIncrementedCount() {
        return service.incrementVisitorCount();
    }
}
