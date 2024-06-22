package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();

    Booking deleteById(Long idRequired);

    Booking save(BookingDTO booking);

    void createBooking();

    void createBooking(Person person);

    void updateBooking();

    List<Booking> getAllBookingsByPersonId(Long id);

    void deleteBooking();
}
