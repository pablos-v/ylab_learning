package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.model.Booking;

import java.util.Optional;

public interface BookingRepository {

    Optional<Booking> findById(Long bookingId);

    void put(Booking original);
}
