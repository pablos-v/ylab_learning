package ru.ylab_learning.coworking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.enums.PersonRole;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.repository.PersonRepository;
import ru.ylab_learning.coworking.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    PersonServiceImpl service;
    PersonRepository repository;
    List<Person> persons;

    @BeforeEach
    void setUp() {
        repository = mock(PersonRepository.class);
        service = new PersonServiceImpl(repository);
        persons = List.of(
                new Person(1L, "login", "pass", PersonRole.USER, "Ivan", "ivanov@mail.ru"),
                new Person(2L,  "login2", "pass2", PersonRole.USER, "Ivan2", "ivanov2@mail.ru"),
                new Person(3L,  "login3",  "pass3", PersonRole.USER,  "Ivan3",  "ivanov3@mail.ru")
        );
    }

    @Test
    @DisplayName("Тест на успешное возвращение сохранённого пользователя")
    void saveSuccess() {
        PersonDTO PersonDTO = new PersonDTO();
        Person expectedPerson = persons.get(0);
        when(repository.save(PersonDTO)).thenReturn(expectedPerson);

        Person result = service.save(PersonDTO);

        assertThat(result).isEqualTo(expectedPerson);
    }
    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void saveIsThrowingPersonNotFoundException() {
        when(repository.save(any())).thenThrow(new PersonNotFoundException());

        assertThatThrownBy(() -> service.save(new PersonDTO())).isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    @DisplayName("Тест на возврат корректного значения из репозитория")
    void getPersonByIdSuccess() {
        Person expectedPerson = persons.get(0);
        when(repository.findById(1L)).thenReturn(Optional.of(expectedPerson));

        Person actualPerson = service.getPersonById(1L);

        assertThat(actualPerson).isEqualTo(expectedPerson);
    }

    @Test
    @DisplayName("Тест на возврат имеющихся в репозитории значений")
    void getAllPersons() {
        when(repository.findAll()).thenReturn(persons);

        List<Person> result = service.getAllPersons();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result).isEqualTo(persons);
    }

    @Test
    @DisplayName("Тест на возврат пустого списка при отсутствии в репозитории значений")
    void getAllPersonsReturnEmpty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<Person> result = service.getAllPersons();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Тест на успешное выполнение")
    void getMaxIdSuccess() {
        when(repository.findAll()).thenReturn(persons);

        long maxId = service.getMaxId();

        assertThat(maxId).isEqualTo(3L);
    }

    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void getMaxIdIsThrowingPersonNotFoundException() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> service.getMaxId()).isInstanceOf(PersonNotFoundException.class);
    }

}