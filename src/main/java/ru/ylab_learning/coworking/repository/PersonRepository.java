package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория пользователей.
 */
public interface PersonRepository {

    /**
     * Метод поиска пользователя по логину.
     * Используется на стадии создания пользователя для проверки уникальности.
     *
     * @param login логин пользователя
     * @return Optional<Person>
     */
    Optional<Person> findByLogin(String login);

    /**
     * Метод сохраниния нового пользователя. Сначала создаёт пользователя на основе DTO.
     * При создании пользователя, ему присваивается новый ID.
     *
     * @param dto DTO пользователя
     * @return созданного пользователя
     * @throws PersonNotFoundException если после сохранения пользователя не существует в БД
     */
    Person save(PersonDTO dto) throws PersonNotFoundException;

    /**
     * Метод поиска всех пользователей.
     *
     * @return список пользователей
     */
    List<Person> findAll();

    /**
     * Метод поиска пользователя по ID.
     *
     * @param personId ID пользователя
     * @return Optional<Person>
     */
    Optional<Person> findById(Long personId);
}
