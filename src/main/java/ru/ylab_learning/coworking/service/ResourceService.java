package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.model.Resource;

import java.util.List;

public interface ResourceService {
    List<Resource> getAllResources();

    Resource getById(Long id);

    void createResource();

    Resource save(ResourceDTO resource);

    void updateResource();

    void deleteResource();

    long getMaxId();
}
