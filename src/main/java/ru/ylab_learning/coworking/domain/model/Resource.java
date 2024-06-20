package ru.ylab_learning.coworking.domain.model;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.ResourceType;

@Data
public class Resource {

    private static long idCounter = 0;

    private Long id;

    private ResourceType type;

    private boolean isActive;

    public Resource(ResourceType type) {
        this.type = type;
        this.isActive = true;
        this.id  = idCounter++;
    }
}
