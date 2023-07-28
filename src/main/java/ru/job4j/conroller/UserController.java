package ru.job4j.conroller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.User;
import ru.job4j.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService users;
    private BCryptPasswordEncoder encoder;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody User person) {
        person.setPassword(encoder.encode(person.getPassword()));
        users.save(person);
    }

    @GetMapping("/all")
    public List<User> findAll() {
        return users.findAll();
    }
}
