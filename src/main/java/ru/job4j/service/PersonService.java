package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class PersonService implements UserDetailsService {

    private final PersonRepository repository;

    public Optional<Person> save(Person person) {
        try {
            return Optional.of(repository.save(person));
        } catch (ConstraintViolationException e) {
            e.getConstraintName();
        }
        return Optional.empty();
    }

    public boolean update(Person person) {
        if (findById(person.getId()).isPresent()) {
            repository.save(person);
            return true;
        }
        return false;
    }

    public Optional<Person> updateByReflection(Person current, Person person)
            throws InvocationTargetException, IllegalAccessException {
        var methods = current.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            String.format("""
                                    Impossible invoke set method from object :
                                    %s, Check set and get pairs.
                                    """, current));
                }
                var newValue = getMethod.invoke(person);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        return Optional.of(repository.save(current));
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var person = repository.findByLogin(login);
        if (person == null) {
            throw new UsernameNotFoundException(login);
        }
        return new User(person.getLogin(), person.getPassword(), emptyList());
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public boolean delete(int id) {
        if (findById(id).isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
