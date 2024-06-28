package ru.ylab_learning.coworking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ResourceServiceImplTest {
    ResourceServiceImpl service;
    ResourceRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(ResourceRepository.class);
        service = new ResourceServiceImpl(repository);
    }

    @Test
    @DisplayName("Тест на возврат имеющихся в репозитории значений")
    void getAllResources() {
        List<Resource> resources = List.of(
                new Resource(1L, ResourceType.WORKPLACE, 100, "Place", true),
                new Resource(2L, ResourceType.MEETING_ROOM, 200, "Room", true)
        );
        when(repository.findAll()).thenReturn(resources);

        List<Resource> result = service.getAllResources();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(resources);
    }
    @Test
    @DisplayName("Тест на возврат пустого списка при отсутствии в репозитории значений")
    void getAllResourcesReturnEmpty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<Resource> result = service.getAllResources();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Тест на возврат корректного значения из репозитория")
    void getByIdSuccess() {
        Resource expectedResource = new Resource(1L, ResourceType.WORKPLACE, 100, "Description", true);
        when(repository.findById(1L)).thenReturn(Optional.of(expectedResource));

        Resource actualResource = service.getById(1L);

        assertThat(actualResource).isEqualTo(expectedResource);
    }
    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void getByIdIsThrowingResourceNotFoundException() {
        assertThatThrownBy(() -> service.getById(-1L)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("Тест на успешное возвращение сохранённого ресурса")
    void saveSuccess() {
        ResourceDTO resourceDTO = new ResourceDTO(ResourceType.MEETING_ROOM, 480, "Description", true);
        Resource expectedResource = new Resource(1L, ResourceType.MEETING_ROOM, 480, "Description", true);
        when(repository.save(resourceDTO)).thenReturn(expectedResource);

        Resource result = service.save(resourceDTO);

        assertThat(result).isEqualTo(expectedResource);
    }
    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void saveIsThrowingResourceNotFoundException() {
        when(repository.save(any())).thenThrow(new ResourceNotFoundException());

        assertThatThrownBy(() -> service.save(new ResourceDTO())).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("Тест на успешное выполнение")
    void getMaxIdSuccess() {
        List<Resource> resources = List.of(
                new Resource(1L, ResourceType.WORKPLACE, 100, "Resource 1", true),
                new Resource(2L, ResourceType.MEETING_ROOM, 200, "Resource 2", true),
                new Resource(3L, ResourceType.WORKPLACE, 300, "Resource 3", true)
        );
        when(repository.findAll()).thenReturn(resources);

        long maxId = service.getMaxId();

        assertThat(maxId).isEqualTo(3L);
    }

    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void getMaxIdIsThrowingResourceNotFoundException() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> service.getMaxId()).isInstanceOf(ResourceNotFoundException.class);
    }

}