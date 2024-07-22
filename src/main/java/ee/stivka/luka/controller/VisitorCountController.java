package ee.stivka.luka.controller;

import ee.stivka.luka.service.VisitorCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
