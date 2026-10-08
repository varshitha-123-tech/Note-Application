package org.practice.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/Register")
    public String register(@RequestBody User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            return "Username already exists";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "User Registered Successfully";
        }
    }

    @PostMapping("/Login")
    public String login(@RequestBody User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        if (foundUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
                return "Login Successful";
            } else {
                return "Wrong password";
            }
        } else {
            return "User not found";
        }
    }

    @GetMapping("/users/{username}/exists")
    public boolean userExists(@PathVariable String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}