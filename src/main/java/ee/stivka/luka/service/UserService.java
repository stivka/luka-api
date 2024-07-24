package ee.stivka.luka.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import ee.stivka.luka.model.User;
import ee.stivka.luka.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DiscordService discordService;
    private final UserRepository userRepository;

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
