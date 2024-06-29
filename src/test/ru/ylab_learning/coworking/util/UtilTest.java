package ru.ylab_learning.coworking.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab_learning.coworking.domain.dto.Slot;
import ru.ylab_learning.coworking.domain.enums.ResourceType;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Resource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {
    LocalDate date;
    List<Booking> bookings;
    @BeforeEach
    void setUp()  {
        date = LocalDate.of(2024, 10, 10);
        bookings = List.of(
                new Booking(1L, 1L, date, LocalTime.of(1, 0), LocalTime.of(2, 0)),
                new Booking(1L, 1L, date, LocalTime.of(3, 0), LocalTime.of(4, 0))
        );
    }

    @Test
    @DisplayName("Тест на корректную проверку email")
    void emailMatchesPattern() {
        assertThat(Util.emailMatchesPattern("test@test.com")).isTrue();
        assertThat(Util.emailMatchesPattern("test@test")).isFalse();
        assertThat(Util.emailMatchesPattern("test@test.com.ru.ru")).isFalse();
        assertThat(Util.emailMatchesPattern("test@test.")).isFalse();
        assertThat(Util.emailMatchesPattern("@test.com")).isFalse();
        assertThat(Util.emailMatchesPattern("test.com")).isFalse();
    }

    @Test
    @DisplayName("Тест на количество возвращаемых слотов при отсутствии броней")
    void getAvailableSlotsReturnsAllSlotsWithNoBookings() {
        List<Resource> resources = List.of(
                new Resource(1L, ResourceType.MEETING_ROOM, 100, "Room 1", true),
                new Resource(2L, ResourceType.MEETING_ROOM, 150, "Room 2", true)
        );
        List<Booking> bookingsEmpty = new ArrayList<>();
        int TWENTY_FOUR_SLOTS_IN_DAY = 24;
        int expected = TWENTY_FOUR_SLOTS_IN_DAY * resources.size();

        List<Slot> availableSlots = Util.getAvailableSlots(resources, date, bookingsEmpty);

        assertThat(availableSlots.size()).isEqualTo(expected);
    }
    @Test
    @DisplayName("Тест на количество возвращаемых слотов при наличии броней")
    void getAvailableSlotsReturnsSlotsWithBookings() {
        List<Resource> resources = List.of(
                new Resource(1L, ResourceType.MEETING_ROOM, 100, "Room 1", true)
        );
        int TWENTY_FOUR_SLOTS_IN_DAY = 24;
        int BOOKED_SLOTS = 2;
        int expected = TWENTY_FOUR_SLOTS_IN_DAY - BOOKED_SLOTS;

        List<Slot> availableSlots = Util.getAvailableSlots(resources, date, bookings);

        assertThat(availableSlots.size()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Тест на наличие кофликта бронирований")
    void hasConflictsTrue() {
        LocalTime startTime = LocalTime.of(1, 0);
        LocalTime endTime = LocalTime.of(3, 0);

        assertThat(Util.hasConflicts(date, bookings, startTime, endTime)).isTrue();
    }
    @Test
    @DisplayName("Тест на отсутствие кофликта бронирований")
    void hasConflictsFalse() {
        LocalTime startTime = LocalTime.of(11, 0);
        LocalTime endTime = LocalTime.of(13, 0);

        assertThat(Util.hasConflicts(date, bookings, startTime, endTime)).isFalse();
    }

}