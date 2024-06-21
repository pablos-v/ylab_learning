package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    Optional<Person> findByLogin(String login);

    void save(Person person);

    List<Person> findAll();
}
