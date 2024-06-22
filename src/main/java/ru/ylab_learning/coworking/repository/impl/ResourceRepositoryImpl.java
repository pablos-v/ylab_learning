package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.repository.ResourceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ResourceRepositoryImpl implements ResourceRepository {

    private final HashMap<Long, Resource> resources = new HashMap<>();

    {
        Resource roomOne = new Resource(ResourceType.MEETING_ROOM, 500, "Большой зал");
        Resource roomTwo = new Resource(ResourceType.MEETING_ROOM, 400, "Малый зал");
        Resource placeOne = new Resource(ResourceType.WORKPLACE, 200, "Комфорт");
        Resource placeTwo = new Resource(ResourceType.WORKPLACE, 200, "Комфорт");
        Resource placeThree = new Resource(ResourceType.WORKPLACE, 250, "Комфорт видовой");
        Resource placeFour = new Resource(ResourceType.WORKPLACE, 300, "Комфорт+ с принтером");
        Resource placeFive = new Resource(ResourceType.WORKPLACE, 300, "Комфорт+ с принтером");
        List.of(roomOne, roomTwo, placeOne, placeTwo, placeThree, placeFour, placeFive)
                .forEach(resource -> resources.put(resource.getId(), resource));
    }
    @Override
    public Optional<Resource> findById(long id) {
        return Optional.of(resources.get(id));
    }

    @Override
    public Optional<Resource> deleteById(Long idRequired) {
        Optional<Resource> response = findById(idRequired);
        if (response.isPresent()) {
            resources.remove(idRequired);
        }
        return response;
    }

    @Override
    public Resource save(ResourceDTO dto) {
        Resource newResource  = new Resource(
                dto.getType(),
                dto.getRentPrice(),
                dto.getDescription()
        );
        resources.put(newResource.getId(), newResource);
        return newResource;
    }

    @Override
    public void update(Resource original) {
        resources.put(original.getId(), original);
    }

    @Override
    public List<Resource> findAll() {
        return resources.values().stream().toList();
    }
}
