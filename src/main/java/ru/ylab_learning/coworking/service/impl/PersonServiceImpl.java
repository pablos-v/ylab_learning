package ru.ylab_learning.coworking.service.impl;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.exception.PersonExistsException;
import ru.ylab_learning.coworking.domain.exception.WrongEmailException;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.PersonRepository;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.util.Util;

import java.util.List;

@Data
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    @Override
    public Person auth() {
        while (true) {
            String[] credentials = ConsoleInput.stringsInput(InputType.AUTH);
            try {
                String login = credentials[0];
                String password = credentials[1];
                Person person = repository.findByLogin(login).orElseThrow(PersonNotFoundException::new);
                if ((person.getPassword().equals(password)))  {
                    return person;
                }
                ConsoleOutput.print("Пароль не подходит!");
            } catch (PersonNotFoundException e) {
                ConsoleOutput.print("Неправильный логин. Попробуйте снова.");
            }
        }
    }

    @Override
    public void createUser() {
        while (true) {
            String[] credentials = ConsoleInput.stringsInput(InputType.REGISTER);
            try {
                if (repository.findByLogin(credentials[0]).isPresent())  {
                    throw new PersonExistsException();
                }
                if (!Util.emailMatchesPattern(credentials[3]))   {
                    throw new WrongEmailException();
                }
                repository.save(new Person(credentials[0], credentials[1], credentials[2], credentials[3]));
                ConsoleOutput.print("Добавлен пользователь " + credentials[2] +
                        " с логином " + credentials[0] + "\n-------------------------\n");
                return;
            } catch (PersonExistsException e) {
                ConsoleOutput.print("Пользователь с логином " + credentials[0] + " уже существует! \nПопробуйте снова.");
            } catch (WrongEmailException e)  {
                ConsoleOutput.print("Неправильный email. Попробуйте снова.");
            }
        }
    }

    @Override
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

}
