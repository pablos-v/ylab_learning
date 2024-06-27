package ru.ylab_learning.coworking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Сущность бронирования
 */
@Data
@AllArgsConstructor
public class Booking {

    private Long id;

    private Long resourceId;

    private Long personId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    public Booking(Long resourceId, Long personId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.resourceId = resourceId;
        this.personId = personId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
