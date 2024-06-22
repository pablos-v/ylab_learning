package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.model.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository {

    Optional<Resource> findById(long id);

    Optional<Resource> deleteById(Long idRequired);

    Resource save(ResourceDTO resource);

    void update(Resource original);

    List<Resource> findAll();
}
