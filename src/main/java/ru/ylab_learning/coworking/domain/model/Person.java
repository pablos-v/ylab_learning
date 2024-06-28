package ru.ylab_learning.coworking.domain.model;

import ru.ylab_learning.coworking.domain.enums.PersonRole;

/**
 * Сущность пользователя
 */
public record Person(Long id, String login, String password, PersonRole role, String name, String email) {
}
