package ru.ylab_learning.coworking.service.impl;

import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.exception.ResourceTypeNotFoundException;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.service.ResourceService;

import java.util.Arrays;
import java.util.List;

/**
 * Реализация сервиса ресурсов
 * @param resourceRepository - репозиторий ресурсов
 */
public record ResourceServiceImpl(ResourceRepository resourceRepository) implements ResourceService {

    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource getById(Long id) throws ResourceNotFoundException{
        return resourceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void createResource() {
        while (true) {
            try {
                ResourceDTO resource = askAndValidate();
                ConsoleOutput.print("Добавлен ресурс: " + save(resource));
                return;
            } catch (ResourceNotFoundException e) {
                ConsoleOutput.print("Ошибка сохранения ресурса. Попробуйте ещё раз.");
            }
        }
    }

    @Override
    public Resource save(ResourceDTO resource) throws ResourceNotFoundException{
        return resourceRepository.save(resource);
    }

    @Override
    public void updateResource() {
        try {
            long maxId = getMaxId();
            Resource original = getById(ConsoleInput.intInput(InputType.ID, maxId));
            boolean status = ConsoleInput.intInput(InputType.RESOURCE_STATUS, 1L) == 1;
            ResourceDTO updated = askAndValidate();
            original.setType(updated.getType());
            original.setDescription(updated.getDescription());
            original.setRentPrice(updated.getRentPrice());
            original.setActive(status);
            resourceRepository.update(original);
            ConsoleOutput.print("Изменен ресурс: " + original);
        } catch (ResourceNotFoundException e) {
            ConsoleOutput.print("Нет ресурсов.");
        }
    }

    @Override
    public void deleteResource() {
        try {
            long maxId = getMaxId();
            Long idRequired = ConsoleInput.intInput(InputType.ID, maxId);
            Resource deleted = resourceRepository.deleteById(idRequired)
                    .orElseThrow(ResourceNotFoundException::new);
            ConsoleOutput.print("Удален ресурс: " + deleted);
        } catch (ResourceNotFoundException e) {
            ConsoleOutput.print("Нет ресурсов.");
        }
    }

    @Override
    public long getMaxId() throws ResourceNotFoundException{
        return getAllResources().stream().mapToLong(Resource::getId).max()
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Метод запрашивает, парсит и валидирует параметры бронирования.
     * Для консольного приложения оказалось трудно разделять ввод, парсинг и валидацию, поэтому метод сложный.
     * @return валидный DTO ресурса
     */
    private ResourceDTO askAndValidate() {
        ResourceDTO resource = new ResourceDTO();
        while (true) {
            String[] input = ConsoleInput.stringsInput(InputType.RESOURCE);
            try {
                if (input[0].equals("place") || input[0].equals("room")) {
                    resource.setType(input[0].equals("place") ? ResourceType.WORKPLACE : ResourceType.MEETING_ROOM);
                } else {
                    throw new ResourceTypeNotFoundException();
                }
                resource.setRentPrice(Integer.parseInt(input[1]));
                resource.setDescription(String.join(" ", Arrays.copyOfRange(input, 2, input.length)));
                resource.setActive(true);
            } catch (NumberFormatException e) {
                ConsoleOutput.print("Неверный формат данных, попробуйте снова");
                continue;
            } catch (ResourceTypeNotFoundException e) {
                ConsoleOutput.print("Неверный тип ресурса, попробуйте снова");
                continue;
            }
            return resource;
        }
    }
}
