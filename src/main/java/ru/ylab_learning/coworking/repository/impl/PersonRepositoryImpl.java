package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.enums.PersonRole;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.repository.PersonRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория пользователей
 */
public class PersonRepositoryImpl implements PersonRepository {
    /**
     * Хранение в памяти
     */
    private final HashMap<Long, Person> persons = new HashMap<>();

    // Иницияция данных для теста приложения
    {
        Person admin = new Person("admin", "password", "Администратор", "admin@coworking.ru");
        admin.setRole(PersonRole.ADMIN);
        persons.put(admin.getId(), admin);
        Person user = new Person("user",  "password",  "Пользователь",  "user@coworking.ru");
        persons.put(user.getId(), user);
    }

    @Override
    public Optional<Person> findByLogin(String login) {
        return persons.values().stream().filter(p -> p.getLogin().equals(login)).findFirst();
    }

    @Override
    public Person save(PersonDTO dto) {
        Person savedPerson = new Person(
                dto.getLogin(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail()
        );
        persons.put(savedPerson.getId(), savedPerson);
        return savedPerson;
    }

    @Override
    public List<Person> findAll() {
        return persons.values().stream().toList();
    }

    @Override
    public Optional<Person> findById(Long personId) {
        return Optional.ofNullable(persons.get(personId));
    }

}
