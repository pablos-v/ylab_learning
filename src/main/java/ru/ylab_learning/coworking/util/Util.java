package ru.ylab_learning.coworking.util;

import ru.ylab_learning.coworking.domain.dto.Slot;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.enums.MenuValues;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Класс-коллекция вспомогательных методов
 */
public class Util {
    /**
     * Запрашивает номер пункта меню
     * @param menu - тип меню
     * @return номер пункта меню
     */
    public static int askNumberForMenu(MenuValues menu) {
        ConsoleOutput.print(String.valueOf(menu));
        return Math.toIntExact(ConsoleInput.intInput(InputType.MENU_NUMBER, (long) menu.getMaxNumberOfMenu()));
    }

    /**
     * Валидация адреса email
     * @param check - проверяемое значение
     * @return true если валидно
     */
    public static boolean emailMatchesPattern(String check) {
        String regexPattern = "\\w+@\\w+\\.\\w+";
        return Pattern.compile(regexPattern).matcher(check).matches();
    }

    /**
     * Заглушка сервиса уведомления пользователя об удалении бронирования
     * @param person - объект пользователя
     */
    public static void notifyUser(Person person) {
        String email = person.email();
        // Отправляем уведомление
    }

    /**
     * Метод, составляющий список свободных слотов по ресурсам на определённую дату.
     * @param resources - список всех ресурсов
     * @param date - дата
     * @param bookings - список имеющихся бронирований
     * @return список свободных слотов
     */
    public static List<Slot> getAvailableSlots(List<Resource> resources, LocalDate date,
            List<Booking> bookings) {
        List<Slot> availableSlots = new ArrayList<>();

        for (Resource resource : resources) {
            // Перебираем все часы с 00:00 до 24:00
            for (int hour = 0; hour < 24; hour++) {
                LocalTime startTime = LocalTime.of(hour, 0);
                int correctCounter = hour == 23 ? 0 : (hour + 1);
                LocalTime endTime = LocalTime.of(correctCounter, 0);

                // Проверяем, не пересекается ли текущий слот с занятыми слотами
                if (!hasConflicts(date, bookings, startTime, endTime)) {
                    availableSlots.add(new Slot(resource.getId(), resource.getType(),
                            resource.getDescription(), startTime, endTime));
                }
            }
        }
        return availableSlots;
    }

    /**
     * Метод определяет, есть ли пересечение с занятыми слотами по времени
     * @param date - дата
     * @param bookings - список имеющихся бронирований
     * @param startTime - время начала
     * @param endTime - время окончания
     * @return true если есть пересечение
     */
    public static boolean hasConflicts(LocalDate date, List<Booking> bookings, LocalTime startTime,
            LocalTime endTime) {
        return bookings.stream()
                .filter(booking -> booking.getDate().equals(date))
                .anyMatch(booking ->
                        (!booking.getStartTime().isBefore(startTime)) // он стартует не до нашего (во время или позже)
                        && booking.getStartTime().isBefore(endTime) // и наш ещё не кончился
                        || booking.getEndTime().isAfter(startTime) // или он закончится после старта нашего
                        && (!booking.getEndTime().isAfter(endTime))); // и до окончания нашего
    }

    /**
     * Метод фильтрует список бронирований по дате и выводит в консоль.
     * @param allBookings - список всех бронирований
     */
    public static void filterBookingsByDate(List<Booking> allBookings) {
        LocalDate dateRequired = ConsoleInput.dateInput();
        List<Booking> filteredBookings = allBookings.stream()
                .filter(booking -> booking.getDate().equals(dateRequired)).toList();
        if (filteredBookings.isEmpty()) {
            ConsoleOutput.print("Нет бронирований на дату: " + dateRequired);
        } else {
            ConsoleOutput.print("Все бронирования на дату: " + dateRequired);
            ConsoleOutput.printList(filteredBookings);
        }
    }

    /**
     * Метод фильтрует список бронирований по пользователю и выводит в консоль.
     * Предварительно запрашивает ID пользователя.
     * @param allBookings - список всех бронирований
     * @param maxId - максимальный ID пользователя (для валидации при вводе)
     */
    public static void filterBookingsByPerson(List<Booking> allBookings, Long maxId) {
        Long idRequired = ConsoleInput.intInput(InputType.ID, maxId);
        List<Booking> filteredBookings = allBookings.stream()
                .filter(booking -> booking.getPersonId().equals(idRequired)).toList();
        if (filteredBookings.isEmpty()) {
            ConsoleOutput.print("Нет бронирований для пользователя с ID: " + idRequired);
        } else {
            ConsoleOutput.print("Все бронирования для пользователя с ID: " + idRequired);
            ConsoleOutput.printList(filteredBookings);
        }
    }

    /**
     * Метод фильтрует список бронирований по ресурсу и выводит в консоль.
     * Предварительно запрашивает ID ресурса.
     * @param allBookings - список всех бронирований
     * @param maxId - максимальный ID ресурса  (для валидации при вводе)
     */
    public static void filterBookingsByResource(List<Booking> allBookings, Long maxId) {
        Long idRequired = ConsoleInput.intInput(InputType.ID, maxId);
        List<Booking> filteredBookings = allBookings.stream()
                .filter(booking -> booking.getResourceId().equals(idRequired)).toList();
        if (filteredBookings.isEmpty()) {
            ConsoleOutput.print("Нет бронирований для ресурса с ID: " + idRequired);
        } else {
            ConsoleOutput.print("Все бронирования для ресурса с ID: " + idRequired);
            ConsoleOutput.printList(filteredBookings);
        }
    }
}
