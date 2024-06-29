package ru.ylab_learning.coworking.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab_learning.coworking.domain.enums.ResourceType;

/**
 * ДТО ресурса
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {

    private ResourceType type;

    private int rentPrice;

    private String description;

    private boolean isActive;
}
