package ru.ylab_learning.coworking.util;

import ru.ylab_learning.coworking.domain.dto.Slot;
import ru.ylab_learning.coworking.domain.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-коллекция вспомогательных методов
 */
public class Util {
    public List<Slot> getAvailableSlots(LocalDate date, List<Booking> bookings) {
        List<Slot> availableSlots = new ArrayList<>();

        // Перебираем все часы с 00:00 до 24:00
        for (int hour = 0; hour < 24; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0);
            LocalTime endTime = LocalTime.of(hour + 1, 0);

            // Проверяем, не пересекается ли текущий слот с занятыми слотами
            if (bookings.stream().filter(booking -> booking.getDate().equals(date))
                    .noneMatch(booking -> booking.getStartTime().isAfter(startTime) &&
                            booking.getStartTime().isBefore(endTime) ||
                            booking.getEndTime().isAfter(startTime) &&
                                    booking.getEndTime().isBefore(endTime))) {
                availableSlots.add(new Slot(startTime, endTime));
            }
        }

        return availableSlots;
}}
