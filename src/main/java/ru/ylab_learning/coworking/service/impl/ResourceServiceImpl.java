package ru.ylab_learning.coworking.service.impl;

import lombok.Data;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.service.ResourceService;

import java.util.List;
@Data
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    @Override
    public List<Resource> getAllResources() {
        return null;
    }
}
