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

    /**
     * Метод поиска пользователя по логину.
     * Используется на стадии создания пользователя для проверки уникальности.
     * @param login логин пользователя
     * @return Optional<Person>
     */
    @Override
    public Optional<Person> findByLogin(String login) {
        return persons.values().stream().filter(p -> p.getLogin().equals(login)).findFirst();
    }

    /**
     * Метод сохраниния нового пользователя. Сначала создаёт пользователя на основе DTO.
     * При создании пользователя, ему присваивается новый ID.
     * @param dto DTO пользователя
     * @return созданного пользователя
     */
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

    /**
     * Метод поиска всех пользователей.
     * @return список пользователей
     */
    @Override
    public List<Person> findAll() {
        return persons.values().stream().toList();
    }

    /**
     * Метод поиска пользователя по ID.
     * @param personId ID пользователя
     * @return Optional<Person>
     */
    @Override
    public Optional<Person> findById(Long personId) {
        return Optional.ofNullable(persons.get(personId));
    }

}
