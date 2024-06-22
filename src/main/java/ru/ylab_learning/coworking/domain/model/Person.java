package ru.ylab_learning.coworking.domain.model;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.PersonRole;

/**
 * Сущность пользователя
 */
@Data
public class Person {

    private static long idCounter = 1L;

    private Long id;
    // уникален
    private String login;

    private String password;

    private PersonRole role;

    private String name;

    private String email;

    public Person(String login, String password, String name, String email) {
        this.id = idCounter++;
        this.login = login;
        this.password = password;
        this.role = PersonRole.USER;
        this.name = name;
        this.email = email;
    }
}
