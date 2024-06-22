package ru.ylab_learning.coworking.service.impl;

import lombok.Data;
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

@Data
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PersonService personService;
    private final ResourceService resourceService;


    private Booking getBookingById(Long bookingId) {
        // TODO check if exists
        return null;
    }

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

    @Override
    public Booking save(BookingDTO booking) {
        // TODO ИД созд в репо и записать
        return null;
    }

    public Booking getById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public Booking createBooking() {
        BookingDTO newBooking = askAndValidate(InputType.ADMIN_NEW_BOOKING);
        return save(newBooking);
    }

    @Override
    public Booking createBooking(Person person) {
        BookingDTO newBooking = askAndValidate(InputType.USER_BOOKING);
        newBooking.setPersonId(person.getId());
        return save(newBooking);
    }

    @Override
    public Booking updateBooking() {
        BookingDTO updated = askAndValidate(InputType.ADMIN_UPDATE_BOOKING);
        Booking original  = getBookingById(updated.getBookingId());
        original.setDate(updated.getDate());
        original.setStartTime(updated.getStartTime());
        original.setEndTime(updated.getEndTime());
        original.setPersonId(updated.getPersonId());
        original.setResourceId(updated.getResourceId());
        bookingRepository.put(original);
        return original;
    }

    private BookingDTO askAndValidate(InputType type) {
        BookingDTO booking = new BookingDTO();
        while (true) {
            // получим строку с ID ресурса, ID пользователя, дату, часы начала и окончания
            String[] input = ConsoleInput.stringsInput(type);
            try {
                // парсим
                booking.setResourceId(Long.parseLong(input[0]));
                booking.setDate(LocalDate.parse(input[1], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                booking.setStartTime(LocalTime.parse(input[2] + "-00", DateTimeFormatter.ofPattern("HH-mm")));
                booking.setEndTime(LocalTime.parse(input[3] + "-00", DateTimeFormatter.ofPattern("HH-mm")));
                if (type == InputType.ADMIN_NEW_BOOKING || type == InputType.ADMIN_UPDATE_BOOKING)  {
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
            if (Util.hasConflicts(booking.getDate(), getAllBookings(), booking.getStartTime(),
                    booking.getEndTime())) {
                ConsoleOutput.print("Выбранный слот занят, попробуйте снова");
                continue;
            }
            return booking;
        }
    }
}
