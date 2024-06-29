package ru.ylab_learning.coworking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.ResourceType;

/**
 * Сущность ресурса
 */
@Data
@AllArgsConstructor
public class Resource {

    private Long id;

    private ResourceType type;

    private int rentPrice;

    private String description;

    private boolean isActive;

}
