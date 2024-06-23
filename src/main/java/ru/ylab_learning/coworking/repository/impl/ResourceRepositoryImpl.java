package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.repository.ResourceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория ресурсов
 */
public class ResourceRepositoryImpl implements ResourceRepository {
    /**
     * Хранение в памяти
     */
    private final HashMap<Long, Resource> resources = new HashMap<>();

    // Иницияция данных для теста приложения
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

    /**
     * Метод поиска ресурса по ID
     * @param id ID ресурса
     * @return Optional<Resource>
     */
    @Override
    public Optional<Resource> findById(long id) {
        return Optional.ofNullable(resources.get(id));
    }

    /**
     * Метод удаления ресурса по его ID
     * @param idRequired ID ресурса
     * @return удалённый ресурс в виде Optional<Resource>
     */
    @Override
    public Optional<Resource> deleteById(Long idRequired) {
        Optional<Resource> response = findById(idRequired);
        if (response.isPresent()) {
            resources.remove(idRequired);
        }
        return response;
    }

    /**
     * Метод сохранения нового ресурса, сначала создаёт новый объект ресурса на основе DTO.
     * При создании ресурса, ему присваивается новый ID.
     * @param dto DTO объекта ресурса
     * @return созданный ресурс
     */
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

    /**
     * Метод изменения ресурса
     * @param original изменённый ресурс
     */
    @Override
    public void update(Resource original) {
        resources.put(original.getId(), original);
    }

    /**
     * Метод поиска всех ресурсов
     * @return список ресурсов
     */
    @Override
    public List<Resource> findAll() {
        return resources.values().stream().toList();
    }
}
