package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.dto.PersonDTO;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;

public interface PersonService {
    
    Person auth();

    void createUser();

    Person save(PersonDTO person);

    List<Person> getAllPersons();

    Long getMaxId();

    Person getPersonById(Long personId);
}
