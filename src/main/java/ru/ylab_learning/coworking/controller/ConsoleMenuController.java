package ru.ylab_learning.coworking.controller;

import ru.ylab_learning.coworking.domain.enums.MenuValues;
import ru.ylab_learning.coworking.domain.enums.PersonRole;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.service.BookingService;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.service.ResourceService;
import ru.ylab_learning.coworking.util.Util;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для консольного меню
 * @param personService сервис пользователей
 * @param bookingService сервис бронирования
 * @param resourceService сервис ресурсов
 */
public record ConsoleMenuController(PersonService personService, BookingService bookingService, ResourceService resourceService) {
    /**
     * Стартовое меню.
     */
    public void startMenu() {
        boolean exit = false;
        while (!exit) {
            int choice = Util.askNumberForMenu(MenuValues.START_MENU);
            switch (choice) {
                case 1 -> {
                    Person person = personService.auth();
                    mainMenu(person);
                }
                case 2 -> {
                    personService.createUser();
                    ConsoleOutput.print("Теперь вы можете войти под своими данными.");
                }
                case 0 -> exit = true;
            }
        }
    }

    /**
     * После аутентификации пользователя, авторизация происходит здесь.
     * @param person объект пользователя
     */
    private void mainMenu(Person person) {
        if (person.getRole().equals(PersonRole.ADMIN)) {
            adminMainMenu();
        } else {
            userMainMenu(person);
        }
    }

    private void adminMainMenu() {
        while (true) {
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_MENU);
            switch (choice) {
                case 1 -> adminBookingsFilterMenu();
                case 2 -> adminResourcesMenu();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void adminBookingsFilterMenu() {
        // вывести все бронирования
        List<Booking> allBookings = bookingService.getAllBookings();
        if (allBookings == null || allBookings.isEmpty()) {
            ConsoleOutput.print("Нет бронирований.");
        } else {
            ConsoleOutput.print("Все бронирования:");
            ConsoleOutput.printList(allBookings);
            // предложить фильтрацию
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_BOOKINGS_FILTER_MENU);
            switch (choice) {
                case 1 -> Util.filterBookingsByDate(allBookings);
                case 2 -> Util.filterBookingsByPerson(allBookings, personService.getMaxId());
                case 3 -> Util.filterBookingsByResource(allBookings, resourceService.getMaxId());
                case 4 -> {
                    // do nothing
                }
                case 0 -> {
                    return;
                }
            }
        }
        adminBookingsMenu();
    }

    private void adminBookingsMenu() {
        while (true) {
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_BOOKINGS_MENU);
            switch (choice) {
                case 1 -> bookingService.deleteBooking();
                case 2 -> bookingService.createBooking();
                case 3 -> bookingService.updateBooking();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void adminResourcesMenu() {
        ConsoleOutput.printList(resourceService.getAllResources());
        while (true) {
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_RESOURCES_MENU);
            switch (choice) {
                case 1 -> resourceService.createResource();
                case 2 -> resourceService.updateResource();
                case 3 -> resourceService.deleteResource();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void userMainMenu(Person person) {
        while (true) {
            int choice = Util.askNumberForMenu(MenuValues.USER_MENU);
            switch (choice) {
                case 1 -> ConsoleOutput.printList(bookingService.getAllBookingsByPersonId(person.getId()));
                case 2 -> ConsoleOutput.printList(resourceService.getAllResources());
                case 3 -> {
                    LocalDate date = ConsoleInput.dateInput();
                    List<Booking> userBookings = bookingService.getAllBookingsByPersonId(person.getId());
                    ConsoleOutput.printList(Util.getAvailableSlots(resourceService.getAllResources(), date, userBookings));
                }
                case 4 -> bookingService.createBooking(person);
                case 0 -> {
                    return;
                }
            }
        }
    }
}
