package ru.ylab_learning.coworking.repository.impl;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.repository.BookingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория бронирований
 */
public class BookingRepositoryImpl implements BookingRepository {
    /**
     * Хранение в памяти
     */
    private final HashMap<Long, Booking> bookings = new HashMap<>();

    // Иницияция данных для теста приложения
    {
        Booking bookingOne = new Booking(1L, 2L, LocalDate.of(2024, 6, 25), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Booking bookingTwo = new Booking(3L, 2L, LocalDate.of(2024, 6, 25), LocalTime.of(13, 0), LocalTime.of(18, 0));
        Booking bookingThree = new Booking(4L, 2L, LocalDate.of(2024, 6, 26), LocalTime.of(12, 0), LocalTime.of(15, 0));
        Booking bookingFour = new Booking(6L, 2L, LocalDate.of(2024, 6, 27), LocalTime.of(11, 0), LocalTime.of(23, 0));
        List.of(bookingOne, bookingTwo, bookingThree, bookingFour)
                .forEach(it-> bookings.put(it.getId(), it));
    }

    /**
     * Метод поиска бронирования по id
     * @param bookingId ID бронирования
     * @return Optional<Booking>
     */
    @Override
    public Optional<Booking> findById(Long bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    /**
     * Метод изменения бронирования
     * @param original изменённое бронирование
     */
    @Override
    public void update(Booking original) {
        bookings.put(original.getId(), original);
    }

    /**
     * Метод поиска всех бронирований по id пользователя
     * @param id id пользователя
     * @return список бронирований
     */
    @Override
    public List<Booking> findAllByPersonId(Long id) {
        return bookings.values().stream().filter(it->it.getPersonId().equals(id)).toList();
    }

    /**
     * Метод сохранения нового бронирования, сначала создаёт новый объект бронирования на основе DTO.
     * При создании бронирования, ему присваивается новый ID.
     * @param dto DTO объекта бронирования
     * @return созданное бронирование
     */
    @Override
    public Booking save(BookingDTO dto) {
        Booking newBooking = new Booking(
                dto.getResourceId(),
                dto.getPersonId(),
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime()
        );
        bookings.put(newBooking.getId(), newBooking);
        return newBooking;
    }

    /**
     * Метод удаления бронирования по его ID
     * @param idRequired ID бронирования
     * @return удалённое бронирование в виде Optional<Booking>
     */
    @Override
    public Optional<Booking> deleteById(Long idRequired) {
        Optional<Booking> response = findById(idRequired);
        if (response.isPresent()) {
            bookings.remove(idRequired);
        }
        return response;
    }

    /**
     * Метод поиска всех бронирований
     * @return список бронирований
     */
    @Override
    public List<Booking> findAll() {
        return bookings.values().stream().toList();
    }
}
