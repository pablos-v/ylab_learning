package ru.ylab_learning.coworking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab_learning.coworking.domain.dto.BookingDTO;
import ru.ylab_learning.coworking.domain.dto.ResourceDTO;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.exception.BookingNotFoundException;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.repository.BookingRepository;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.service.ResourceService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {
    BookingRepository bookingRepository;
    PersonService personService;
    ResourceService resourceService;

    BookingServiceImpl bookingService;
    List<Booking> bookings;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        personService = mock(PersonService.class);
        resourceService = mock(ResourceService.class);
        bookingService = new BookingServiceImpl(bookingRepository, personService, resourceService);
        bookings = List.of(new Booking(1L, 1L, LocalDate.now(), LocalTime.now(),
                LocalTime.now().plusHours(1)));
    }

    @Test
    @DisplayName("Тест на возврат имеющихся в репозитории значений")
    void getAllBookings() {
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(bookings);
    }

    @Test
    @DisplayName("Тест на возврат пустого списка при отсутствии в репозитории значений")
    void getAllBookingsReturnEmpty() {
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());

        List<Booking> result = bookingService.getAllBookings();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void deleteByIdIsThrowingBookingNotFoundException() {
        Long id = 1L;
        when(bookingRepository.deleteById(id)).thenThrow(new BookingNotFoundException());

        assertThatThrownBy(() -> bookingRepository.deleteById(id)).isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    @DisplayName("Тест на успешное возвращение сохранённого бронирования")
    void deleteByIdSuccess() {
        Long id = 1L;
        Booking expected = bookings.get(0);
        when(bookingRepository.deleteById(id)).thenReturn(Optional.of(expected));

        Booking result = bookingService.deleteById(id);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Тест на успешное возвращение сохранённого бронирования")
    void saveSuccess() {
        BookingDTO booking = new BookingDTO();
        Booking expected = bookings.get(0);
        when(bookingRepository.save(booking)).thenReturn(expected);

        Booking result = bookingService.save(booking);

        assertThat(result).isEqualTo(expected);
    }
    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void saveIsThrowingBookingNotFoundException() {
        when(bookingRepository.save(any())).thenThrow(new BookingNotFoundException());

        assertThatThrownBy(() -> bookingService.save(new BookingDTO())).isInstanceOf(BookingNotFoundException.class);
    }


    @Test
    @DisplayName("Тест на возврат корректного значения из репозитория")
    void getByIdSuccess() {
        Booking expected = bookings.get(0);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(expected));

        Booking actualBooking = bookingService.getById(1L);

        assertThat(actualBooking).isEqualTo(expected);
    }
    @Test
    @DisplayName("Тест на выбрасывание исключения")
    void getByIdIsThrowingBookingNotFoundException() {
        assertThatThrownBy(() -> bookingService.getById(-1L)).isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    @DisplayName("Тест на возврат корректного значения из репозитория")
    void getAllBookingsByPersonIdSuccess() {
        Long personId = 1L;
        when(bookingRepository.findAllByPersonId(personId)).thenReturn(bookings);

        List<Booking> actualBookings = bookingService.getAllBookingsByPersonId(personId);
        
        assertThat(actualBookings).isEqualTo(bookings);
    }
    @Test
    @DisplayName("Тест на возврат пустого списка при отсутствии в репозитории значений")
    void getAllBookingsByPersonIdReturnsEmpty() {
        when(bookingRepository.findAllByPersonId(any())).thenReturn(new ArrayList<>());

        List<Booking> result = bookingService.getAllBookingsByPersonId(-1L);
        
        assertThat(result).isEmpty();
    }

}