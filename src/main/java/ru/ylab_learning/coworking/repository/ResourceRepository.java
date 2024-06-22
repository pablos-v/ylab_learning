package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.model.Resource;

import java.util.Optional;

public interface ResourceRepository {

    Optional<Resource> findById(long l);
}
