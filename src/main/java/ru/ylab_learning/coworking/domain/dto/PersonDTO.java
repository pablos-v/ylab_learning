package ru.ylab_learning.coworking.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ДТО пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String login;

    private String password;

    private String name;

    private String email;
}
