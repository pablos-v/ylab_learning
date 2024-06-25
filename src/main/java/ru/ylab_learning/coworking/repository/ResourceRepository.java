package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.model.Resource;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория ресурсов
 */
public interface ResourceRepository {
    /**
     * Метод поиска ресурса по ID
     *
     * @param id ID ресурса
     * @return Optional<Resource>
     */
    Optional<Resource> findById(long id);

    /**
     * Метод удаления ресурса по его ID
     *
     * @param idRequired ID ресурса
     * @return удалённый ресурс в виде Optional<Resource>
     */
    Optional<Resource> deleteById(Long idRequired);

    /**
     * Метод сохранения нового ресурса, сначала создаёт новый объект ресурса на основе DTO.
     * При создании ресурса, ему присваивается новый ID.
     *
     * @param dto DTO объекта ресурса
     * @return созданный ресурс
     */
    Resource save(ResourceDTO dto);

    /**
     * Метод изменения ресурса
     *
     * @param original изменённый ресурс
     */
    void update(Resource original);

    /**
     * Метод поиска всех ресурсов
     *
     * @return список ресурсов
     */
    List<Resource> findAll();
}
