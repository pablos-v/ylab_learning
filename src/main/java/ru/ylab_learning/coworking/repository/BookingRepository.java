package ru.ylab_learning.coworking.repository;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.model.Booking;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория бронирований.
 */
public interface BookingRepository {
    /**
     * Метод поиска бронирования по id
     *
     * @param bookingId ID бронирования
     * @return Optional<Booking>
     */
    Optional<Booking> findById(Long bookingId);

    /**
     * Метод изменения бронирования
     *
     * @param original изменённое бронирование
     */
    void update(Booking original);

    /**
     * Метод поиска всех бронирований по id пользователя
     *
     * @param id id пользователя
     * @return список бронирований
     */
    List<Booking> findAllByPersonId(Long id);

    /**
     * Метод сохранения нового бронирования, сначала создаёт новый объект бронирования на основе DTO.
     * При создании бронирования, ему присваивается новый ID.
     *
     * @param dto DTO объекта бронирования
     * @return созданное бронирование
     */
    Booking save(BookingDTO dto);

    /**
     * Метод удаления бронирования по его ID
     *
     * @param idRequired ID бронирования
     * @return удалённое бронирование в виде Optional<Booking>
     */
    Optional<Booking> deleteById(Long idRequired);

    /**
     * Метод поиска всех бронирований
     *
     * @return список бронирований
     */
    List<Booking> findAll();
}
