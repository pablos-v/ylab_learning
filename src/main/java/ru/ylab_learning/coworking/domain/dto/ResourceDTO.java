package ru.ylab_learning.coworking.domain.dto;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.ResourceType;

@Data
public class ResourceDTO {

    private Long id;

    private ResourceType type;

    private int rentPrice;

    private String description;

    private boolean isActive;
}
