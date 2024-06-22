package ru.ylab_learning.coworking.service.impl;

import lombok.Data;
import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.exception.BookingNotFoundException;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.exception.ResourceTypeNotFoundException;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.service.ResourceService;
import ru.ylab_learning.coworking.util.Util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Data
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    public List<Resource> getAllResources() {
        return null;
    }

    @Override
    public Resource getById(Long id) {
        return resourceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Resource createResource() {
        ResourceDTO resource = askAndValidate();
        return save(resource);

    }

    @Override
    public Resource updateResource() {
        Long maxId =
                getAllResources().stream().mapToLong(Resource::getId).max().orElseThrow(ResourceNotFoundException::new);
        Resource original = getById(ConsoleInput.intInput(InputType.ID, maxId));
        boolean status = ConsoleInput.intInput(InputType.RESOURCE_STATUS, 1L) == 1;
        ResourceDTO updated = askAndValidate();
        original.setType(updated.getType());
        original.setDescription(updated.getDescription());
        original.setRentPrice(updated.getRentPrice());
        original.setActive(status);
        return put(original);
    }

    @Override
    public Resource deleteResource() {
        return null;
    }

    private ResourceDTO askAndValidate() {
        ResourceDTO resource = new ResourceDTO();
        while (true) {
            String[] input = ConsoleInput.stringsInput(InputType.RESOURCE);
            try {
                // парсим
                if (input[0].equals("place") || input[0].equals("room")) {
                    resource.setType(input[0].equals("place") ? ResourceType.WORKPLACE : ResourceType.MEETING_ROOM);
                } else {
                    throw new ResourceTypeNotFoundException();
                }
                resource.setRentPrice(Integer.parseInt(input[1]));
                resource.setDescription(String.join(" ", Arrays.copyOfRange(input, 2, input.length)));

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
