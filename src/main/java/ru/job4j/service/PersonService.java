package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Person save(Person person) {
        return repository.save(person);
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public void delete(Person person) {
        repository.delete(person);
    }
}
