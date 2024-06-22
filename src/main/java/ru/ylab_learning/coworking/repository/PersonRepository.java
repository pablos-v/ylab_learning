package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    Optional<Person> findByLogin(String login);

    Person save(PersonDTO person);

    List<Person> findAll();

    Optional<Person> findById(Long personId);
}
