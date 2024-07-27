package ee.stivka.luka.service;

import java.util.Optional;

import ee.stivka.luka.model.Login;
import ee.stivka.luka.repository.LoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import ee.stivka.luka.model.User;
import ee.stivka.luka.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final DiscordService discordService;
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;

    @Transactional
    public User login(@RequestBody User possibleUser) {
        if (possibleUser.getUsername().isBlank()) {
            return null;
        }

        Optional<User> userOptional = userRepository.findByUsername(possibleUser.getUsername());
        User user = userOptional.orElseGet(() -> {
            User newUser = userRepository.save(possibleUser);
            discordService.notifyNewUser(newUser.getUsername());
            return newUser;
        });

        loginRepository.save(new Login(user));
        return user;
    }
}
