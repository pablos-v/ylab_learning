package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.model.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();

    Booking deleteById(Long idRequired);
}
