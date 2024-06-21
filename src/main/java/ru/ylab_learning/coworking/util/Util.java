package ru.ylab_learning.coworking.util;

import ru.ylab_learning.coworking.domain.dto.Slot;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.enums.MenuValues;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;
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
    public static int askNumberForMenu(MenuValues menu) {
        ConsoleOutput.print(String.valueOf(menu));
        return Math.toIntExact(ConsoleInput.intInput(InputType.MENU_NUMBER, (long) menu.getMaxNumberOfMenu()));
    }

    public static boolean emailMatchesPattern(String check) {
        String regexPattern = "\\w+@\\w+\\.\\w+";
        return Pattern.compile(regexPattern).matcher(check).matches();
    }

    /**
     * Заглушка сервиса уведомления пользователя об удалении бронирования
     * @param person - объект пользователя
     */
    public static void notifyUser(Person person) {
        String email = person.getEmail();
        // Отправляем уведомление
    }

    public List<Slot> getAvailableSlots(LocalDate date, List<Booking> bookings) {
        List<Slot> availableSlots = new ArrayList<>();

        // Перебираем все часы с 00:00 до 24:00
        for (int hour = 0; hour < 24; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0);
            LocalTime endTime = LocalTime.of(hour + 1, 0);

            // Проверяем, не пересекается ли текущий слот с занятыми слотами
            if (bookings.stream().filter(booking -> booking.getDate().equals(date))
                    .noneMatch(booking -> booking.getStartTime().isAfter(startTime) &&
                            booking.getStartTime().isBefore(endTime) ||
                            booking.getEndTime().isAfter(startTime) &&
                                    booking.getEndTime().isBefore(endTime))) {
                availableSlots.add(new Slot(startTime, endTime));
            }
        }
        return availableSlots;
    }


}
