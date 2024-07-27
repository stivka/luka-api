package ee.stivka.luka.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.stivka.luka.model.User;
import ee.stivka.luka.service.LoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final LoginService loginService;

    @PostMapping()
    public User login(@RequestBody User user) {
        return loginService.login(user);
    }
}
