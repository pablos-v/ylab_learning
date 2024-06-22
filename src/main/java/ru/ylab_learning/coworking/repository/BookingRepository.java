package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    Optional<Booking> findById(Long bookingId);

    void update(Booking original);

    List<Booking> findAllByPersonId(Long id);

    Booking save(BookingDTO booking);

    Optional<Booking> deleteById(Long idRequired);

    List<Booking> findAll();
}
