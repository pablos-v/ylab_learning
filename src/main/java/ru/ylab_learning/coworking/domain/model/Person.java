package ru.ylab_learning.coworking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.PersonRole;

/**
 * Сущность пользователя
 */
@Data
@AllArgsConstructor
public class Person {
// TODO check if final ok then -> to record
    private final Long id;
    // уникален
    private final String login;

    private final String password;

    private final PersonRole role;

    private final String name;

    private final String email;

}
