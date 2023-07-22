package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Optional<Person> save(Person person) {
        try {
            return Optional.of(repository.save(person));
        } catch (ConstraintViolationException e) {
            return Optional.empty();
        }
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public boolean delete(int id) {
        repository.deleteById(id);
        return findById(id).isEmpty();
    }
}
