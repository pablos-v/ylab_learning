package ru.ylab_learning.coworking.service.impl;

import lombok.Data;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.repository.BookingRepository;
import ru.ylab_learning.coworking.service.BookingService;

import java.util.List;
@Data
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    @Override
    public List<Booking> getAllBookings() {
        // TODO
        return null;
    }

    @Override
    public Booking deleteById(Long idRequired) {
        // TODO check if exists
        return null;
    }
}
