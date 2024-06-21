package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.model.Person;

import java.util.Collection;
import java.util.List;

public interface PersonService {
    
    Person auth();

    void createUser();

    List<Person> getAllPersons();

    Person getPersonById(Long personId);
}
