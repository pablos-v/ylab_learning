package ru.ylab_learning.coworking.domain.dto;

import ru.ylab_learning.coworking.domain.enums.ResourceType;

import java.time.LocalTime;

/**
 * ДТО слота бронирования
 * @param id ID ресурса бронирования
 * @param type Тип ресурса бронирования
 * @param description Описание ресурса бронирования
 * @param startTime Начало времени бронирования
 * @param endTime Окончание времени бронирования
 */
public record Slot(Long id, ResourceType type, String description, LocalTime startTime, LocalTime endTime) {
}
