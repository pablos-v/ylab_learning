package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.model.Resource;

import java.util.List;

/**
 * Интерфейс сервиса для работы с ресурсами.
 */
public interface ResourceService {
    /**
     * Метод получения всех ресурсов из БД.
     *
     * @return список всех ресурсов
     */
    List<Resource> getAllResources();

    /**
     * Метод получения ресурса по его ID.
     *
     * @param id ID ресурса
     * @return объект ресурса
     * @throws ResourceNotFoundException если ресурс не найден
     */
    Resource getById(Long id);

    /**
     * Метод создания ресурса. Сначала запрашивает и валидирует параметры ресурса,
     * сохраняя их в промежуточный объект DTO.
     */
    void createResource();

    /**
     * Метод сохранения ресурса на основе DTO.
     *
     * @param resource DTO ресурса
     * @return объект сохранённого ресурса
     */
    Resource save(ResourceDTO resource);

    /**
     * Метод обновления ресурса на основе DTO. Сначала запрашивает и валидирует параметры ресурса,
     * сохраняя их в промежуточный объект DTO.
     */
    void updateResource();

    /**
     * Метод удаления ресурса. Сначала запрашивает ID ресурса.
     */
    void deleteResource();

    /**
     * Метод получения максимального идентификатора ресурса из БД.
     * Нужен для предварительной валидации при вводе данных на запрос ID.
     *
     * @return максимальный идентификатор ресурса
     * @throws ResourceNotFoundException если ресурса нет
     */
    long getMaxId();
}
