package ru.ylab_learning.coworking.service.impl;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.exception.PersonExistsException;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.exception.WrongEmailException;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.PersonRepository;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.util.Util;

import java.util.List;

/**
 * Реализация сервиса пользователей
 * @param repository - репозиторий пользователей
 */
public record PersonServiceImpl(PersonRepository repository) implements PersonService {
    /**
     * Метод аутентификации. В цикле запрашивает логин и пароль, и проверяет валидность.
     * @return объект пользователя
     */
    @Override
    public Person auth() {
        while (true) {
            String[] credentials = ConsoleInput.stringsInput(InputType.AUTH);
            try {
                String login = credentials[0];
                String password = credentials[1];
                Person person = repository.findByLogin(login).orElseThrow(PersonNotFoundException::new);
                if ((person.getPassword().equals(password))) {
                    return person;
                }
                ConsoleOutput.print("Пароль не подходит!");
            } catch (PersonNotFoundException e) {
                ConsoleOutput.print("Неправильный логин. Попробуйте снова.");
            }
        }
    }

    /**
     * Метод создания пользователя. Циклически запрашивает данные.
     * Выход из цикла только при успешном создании пользователя.
     */
    @Override
    public void createUser() {
        while (true) {
            String[] credentials = ConsoleInput.stringsInput(InputType.REGISTER);
            PersonDTO newPerson = new PersonDTO(
                    credentials[0],
                    credentials[1],
                    credentials[2],
                    credentials[3]
            );
            try {
                if (repository.findByLogin(newPerson.getLogin()).isPresent()) {
                    throw new PersonExistsException();
                }
                if (!Util.emailMatchesPattern(newPerson.getEmail())) {
                    throw new WrongEmailException();
                }
                Person savedPerson = save(newPerson);
                ConsoleOutput.print("Добавлен пользователь " + savedPerson.getName() + " с логином " +
                        savedPerson.getLogin() + " и ID " + savedPerson.getId() + "\n-------------------------\n");
                return;
            } catch (PersonExistsException e) {
                ConsoleOutput.print("Пользователь с логином " + newPerson.getLogin() +
                        " уже существует! \nПопробуйте снова.");
            } catch (WrongEmailException e) {
                ConsoleOutput.print("Неправильный email. Попробуйте снова.");
            }
        }
    }

    /**
     * Метод сохранения пользователя на основе DTO.
     * @param person DTO пользователя
     * @return объект пользователя
     */
    @Override
    public Person save(PersonDTO person) {
        return repository.save(person);
    }

    /**
     * Метод получения всех пользователей из БД.
     * @return список всех пользователей
     */
    @Override
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    /**
     * Метод получения максимального идентификатора пользователя из БД.
     * Нужен для предварительной валидации при вводе данных на запрос ID.
     * @return максимальный идентификатор пользователя
     * @throws PersonNotFoundException если пользователей нет
     */
    @Override
    public Long getMaxId() throws PersonNotFoundException{
        return getAllPersons().stream()
                .mapToLong(Person::getId)
                .max().orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Метод получения пользователя по его ID.
     * @param personId ID пользователя
     * @return объект пользователя
     * @throws PersonNotFoundException если пользователь не найден
     */
    @Override
    public Person getPersonById(Long personId) throws PersonNotFoundException{
        return repository.findById(personId).orElseThrow(PersonNotFoundException::new);
    }

}
