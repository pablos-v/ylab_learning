package ru.ylab_learning.coworking.service.impl;

import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.exception.BookingNotFoundException;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.repository.BookingRepository;
import ru.ylab_learning.coworking.service.BookingService;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.service.ResourceService;
import ru.ylab_learning.coworking.util.Util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Реализация сервиса бронирования
 * @param bookingRepository - репозиторий бронирования
 * @param personService - репозиторий пользователей
 * @param resourceService - репозитория ресурсов
 */
public record BookingServiceImpl(BookingRepository bookingRepository, PersonService personService, ResourceService resourceService) implements BookingService {

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking deleteById(Long bookingId) throws BookingNotFoundException{
        return bookingRepository.deleteById(bookingId).orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public Booking save(BookingDTO booking) {
        return bookingRepository.save(booking);
    }
@Override
    public Booking getById(Long bookingId) throws BookingNotFoundException{
        return bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public List<Booking> getAllBookingsByPersonId(Long id) {
        return bookingRepository.findAllByPersonId(id);
    }

    @Override
    public void deleteBooking() {
        try {
            long maxId = getAllBookings().stream()
                    .mapToLong(Booking::getId)
                    .max().orElseThrow(BookingNotFoundException::new);
            Long idRequired = ConsoleInput.intInput(InputType.ID, maxId);
            Booking deleted = deleteById(idRequired);
            ConsoleOutput.print("Удалено бронирование: " + deleted);
            Util.notifyUser(personService.getPersonById(deleted.getPersonId()));
        } catch (BookingNotFoundException e) {
            ConsoleOutput.print("Нет бронирований.");
        }
    }

    @Override
    public void createBooking() {
        BookingDTO newBooking = askAndValidate(InputType.ADMIN_NEW_BOOKING);
        ConsoleOutput.print("Добавлено бронирование: " + save(newBooking));
        Util.notifyUser(personService.getPersonById(newBooking.getPersonId()));
    }

    @Override
    public void createBooking(Person person) {
        BookingDTO newBooking = askAndValidate(InputType.USER_BOOKING);
        newBooking.setPersonId(person.getId());
        save(newBooking);
        ConsoleOutput.print("Создано бронирование: " + newBooking);
    }

    @Override
    public void updateBooking() {
        BookingDTO updated = askAndValidate(InputType.ADMIN_UPDATE_BOOKING);
        Booking original;
        try {
            original = getById(updated.getBookingId());
            original.setDate(updated.getDate());
            original.setStartTime(updated.getStartTime());
            original.setEndTime(updated.getEndTime());
            original.setPersonId(updated.getPersonId());
            original.setResourceId(updated.getResourceId());
            bookingRepository.update(original);
            ConsoleOutput.print("Изменено бронирование: " + original);
            Util.notifyUser(personService.getPersonById(original.getPersonId()));
        } catch (BookingNotFoundException e) {
            ConsoleOutput.print("Бронирование с ID " + updated.getBookingId() + " не найдено");
        }
    }

    /**
     * Метод запрашивает, парсит и валидирует параметры бронирования.
     * Для консольного приложения оказалось трудно разделять ввод, парсинг и валидацию, поэтому метод сложный.
     * @param type тип ввода
     * @return валидный DTO бронирования
     */
    private BookingDTO askAndValidate(InputType type) {
        BookingDTO booking = new BookingDTO();
        while (true) {
            String[] input = ConsoleInput.stringsInput(type);
            try {
                booking.setResourceId(Long.parseLong(input[0]));
                booking.setDate(LocalDate.parse(input[1], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                booking.setStartTime(LocalTime.parse(input[2] + "-00", DateTimeFormatter.ofPattern("HH-mm")));
                booking.setEndTime(LocalTime.parse(input[3] + "-00", DateTimeFormatter.ofPattern("HH-mm")));
                if (type == InputType.ADMIN_NEW_BOOKING || type == InputType.ADMIN_UPDATE_BOOKING) {
                    booking.setPersonId(Long.parseLong(input[4]));
                    personService.getPersonById(booking.getPersonId());
                }
                if (type == InputType.ADMIN_UPDATE_BOOKING) {
                    booking.setBookingId(Long.parseLong(input[5]));
                    getById(booking.getBookingId());
                }
                resourceService.getById(booking.getResourceId());
                // проверка, что время начала и окончания не совпадают и идут хронологически
                if (!booking.getEndTime().isAfter(booking.getStartTime())) {
                    ConsoleOutput.print("Время окончания должно быть позже времени начала");
                    continue;
                }
            } catch (NumberFormatException e) {
                ConsoleOutput.print("Неверный формат данных, попробуйте снова");
                continue;
            } catch (PersonNotFoundException e) {
                ConsoleOutput.print("Пользователя с ID " + booking.getPersonId() + " нет, попробуйте снова");
                continue;
            } catch (ResourceNotFoundException e) {
                ConsoleOutput.print("Ресурса с ID " + booking.getResourceId() + " нет, попробуйте снова");
                continue;
            } catch (BookingNotFoundException e) {
                ConsoleOutput.print("Бронирования с ID " + booking.getBookingId() + " нет, попробуйте снова");
                continue;
            }
            // проверить кофликты
            if (Util.hasConflicts(booking.getDate(), getAllBookings(), booking.getStartTime(), booking.getEndTime())) {
                ConsoleOutput.print("Выбранный слот занят, попробуйте снова");
                continue;
            }
            return booking;
        }
    }
}
