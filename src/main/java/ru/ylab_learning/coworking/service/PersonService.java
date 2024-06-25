package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;

/**
 * Интерфейс сервиса для работы с пользователями.
 */
public interface PersonService {
    /**
     * Метод аутентификации. В цикле запрашивает логин и пароль, и проверяет валидность.
     *
     * @return объект пользователя
     */
    Person auth();

    /**
     * Метод создания пользователя. Циклически запрашивает данные.
     * Выход из цикла только при успешном создании пользователя.
     */
    void createUser();

    /**
     * Метод сохранения пользователя на основе DTO.
     *
     * @param person DTO пользователя
     * @return объект пользователя
     */
    Person save(PersonDTO person);

    /**
     * Метод получения всех пользователей из БД.
     *
     * @return список всех пользователей
     */
    List<Person> getAllPersons();

    /**
     * Метод получения максимального идентификатора пользователя из БД.
     * Нужен для предварительной валидации при вводе данных на запрос ID.
     *
     * @return максимальный идентификатор пользователя
     * @throws PersonNotFoundException если пользователей нет
     */
    Long getMaxId();

    /**
     * Метод получения пользователя по его ID.
     *
     * @param personId ID пользователя
     * @return объект пользователя
     * @throws PersonNotFoundException если пользователь не найден
     */
    Person getPersonById(Long personId);
}
