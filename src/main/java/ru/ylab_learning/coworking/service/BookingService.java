package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();

    Booking deleteById(Long idRequired);

    Booking save(BookingDTO booking);

    Booking createBooking();

    Booking createBooking(Person person);

    Booking updateBooking();
}
