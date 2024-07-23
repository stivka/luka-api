package ee.stivka.luka.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.stivka.luka.model.User;
import ee.stivka.luka.repository.UserRepository;
import ee.stivka.luka.service.DiscordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private DiscordService discordService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public User login(@RequestBody User user) {
        if (user.getUsername().isBlank()) {
            System.out.println("User tried to log in, but no username provided");
            return null;
        }

        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            System.out.println("Existing user logged in");
            User existingUser = userOptional.get();
            existingUser.updateLoginDate();
            userRepository.save(existingUser);
            return existingUser;
        }

        System.out.println("New user logged in, creating account");
        userRepository.save(user);
        discordService.notifyNewUser(user.getUsername());

        return user;
    }
}
