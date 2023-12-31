package ru.job4j.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dto.PersonDTO;
import ru.job4j.model.Person;
import ru.job4j.service.PersonService;
import ru.job4j.validation.Operation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PersonController.class.getSimpleName());

    private final ObjectMapper objectMapper;

    private final PersonService persons;

    @GetMapping("/all")
    public List<Person> findAll() {
        return persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return ResponseEntity.ok(persons.findById(id).get());
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Void> create(@Valid @RequestBody Person person) {
        if (persons.save(person).isPresent()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        if (person.getPassword().length() == 0 || person.getLogin().length() == 0) {
            throw new NullPointerException("Password or login is empty");
        }
        if (persons.update(person)) {
           return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(@RequestBody PersonDTO person) {
        if (persons.updatePassword(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (persons.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public void constraintException(Exception e, HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", "A user with the same name already exists");
            put("details", e.getMessage());
        }}));
        LOGGER.error(e.getMessage());
    }
}
