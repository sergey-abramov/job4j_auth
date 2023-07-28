package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.repository.UserStore;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserStore users;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ru.job4j.model.User user = users.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getUsername(), user.getPassword(), emptyList());
    }

    public void save(ru.job4j.model.User person) {
        users.save(person);
    }

    public List<ru.job4j.model.User> findAll() {
        return users.findAll();
    }
}
