package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.dto.PersonDTO;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class PersonService implements UserDetailsService {

    private final PersonRepository repository;

    private BCryptPasswordEncoder encoder;

    public Optional<Person> save(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
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

    public Optional<Person> updatePassword(PersonDTO personDto) {
        var currentOptional = repository.findById(personDto.getId());
        Person current = new Person();
        if (currentOptional.isPresent()) {
            current = currentOptional.get();
            current.setPassword(encoder.encode(personDto.getPassword()));
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
