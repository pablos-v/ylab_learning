package ru.ylab_learning.coworking.domain.dto;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.ResourceType;

/**
 * ДТО ресурса
 */
@Data
public class ResourceDTO {

    private ResourceType type;

    private int rentPrice;

    private String description;

    private boolean isActive;
}
