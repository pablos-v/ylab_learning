package ru.ylab_learning.coworking.service;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.exception.BookingNotFoundException;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;

import java.util.List;

/**
 * Интерфейс сервиса для работы с бронированием.
 */
public interface BookingService {
    /**
     * Получение всех бронирований из репозитория
     *
     * @return список всех бронирований
     */
    List<Booking> getAllBookings();

    /**
     * Удаление бронирования по ID
     *
     * @param bookingId - ID бронирования
     * @return удалённое бронирование
     * @throws BookingNotFoundException - если бронирование не найдено,
     * исключение пробрасывается выше для корректной обработки
     */
    Booking deleteById(Long bookingId);

    /**
     * Сохранение бронирования
     *
     * @param booking DTO бронирования
     * @return сохранённый объект бронирования
     */
    Booking save(BookingDTO booking);

    /**
     * Метод создания бронирования администратором. Сначала запрашивает и валидирует параметры бронирования,
     * сохраняя их в промежуточный объект DTO.
     * При удачном сохранении вызывает метод уведомления пользователя - владельца бронирования.
     */
    void createBooking();

    /**
     * Метод создания бронирования пользователем. Сначала запрашивает и валидирует параметры бронирования,
     * сохраняя их в промежуточный объект DTO.
     *
     * @param person объект пользователя
     */
    void createBooking(Person person);

    /**
     * Метод обновления бронирования администратором. Сначала запрашивает и валидирует параметры бронирования,
     * сохраняя их в промежуточный объект DTO.
     * При удачном изменении вызывает метод уведомления пользователя - владельца бронирования.
     */
    void updateBooking();

    /**
     * Получение бронирования по ID
     *
     * @param bookingId ID бронирования
     * @return объект бронирования
     * @throws BookingNotFoundException - если бронирование не найдено,
     * исключение пробрасывается выше для корректной обработки
     */
    Booking getById(Long bookingId) throws BookingNotFoundException;

    /**
     * Получение всех бронирований пользователя по его ID
     *
     * @param id ID пользователя
     * @return список бронирований
     */
    List<Booking> getAllBookingsByPersonId(Long id);

    /**
     * Удаление бронирования. Сначала запрашивает ID бронирования, и если такого бронирования нет, то сообщит.
     * При удачном удалении вызывает метод уведомления пользователя.
     */
    void deleteBooking();
}
