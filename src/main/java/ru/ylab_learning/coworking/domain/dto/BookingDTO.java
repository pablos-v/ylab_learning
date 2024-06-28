package ru.ylab_learning.coworking.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ДТО бронирования
 */
@Data
public class BookingDTO {
    private Long resourceId;
    private Long personId;
    private Long bookingId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

}
