package ru.ylab_learning.coworking.domain.model;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.ResourceType;

/**
 * Сущность ресурса
 */
@Data
public class Resource {

    private static long idCounter = 1L;

    private Long id;

    private ResourceType type;

    private int rentPrice;

    private String description;

    private boolean isActive;

    public Resource(ResourceType type, int rentPrice, String description) {
        this.id = idCounter++;
        this.isActive = true;
        this.type = type;
        this.rentPrice = rentPrice;
        this.description = description;
    }
}
