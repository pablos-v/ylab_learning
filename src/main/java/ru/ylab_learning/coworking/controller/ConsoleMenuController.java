package ru.ylab_learning.coworking.controller;

import lombok.Data;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.domain.enums.PersonRole;
import ru.ylab_learning.coworking.domain.exception.PersonNotFoundException;
import ru.ylab_learning.coworking.domain.exception.ResourceNotFoundException;
import ru.ylab_learning.coworking.domain.model.Booking;
import ru.ylab_learning.coworking.domain.model.Person;
import ru.ylab_learning.coworking.domain.model.Resource;
import ru.ylab_learning.coworking.out.ConsoleOutput;
import ru.ylab_learning.coworking.service.BookingService;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.domain.enums.MenuValues;
import ru.ylab_learning.coworking.in.ConsoleInput;
import ru.ylab_learning.coworking.service.ResourceService;
import ru.ylab_learning.coworking.util.Util;

import java.time.LocalDate;
import java.util.List;

@Data
public class ConsoleMenuController {

    private final PersonService personService;
    private final BookingService bookingService;
    private final ResourceService resourceService;
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
        if (allBookings == null || allBookings.isEmpty())  {
            ConsoleOutput.print("Нет бронирований.");
        } else {
            ConsoleOutput.print("Все бронирования:");
            ConsoleOutput.printList(allBookings);
            // предложить фильтрацию
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_BOOKINGS_FILTER_MENU);
            switch (choice) {
                case 1 -> filterBookingsByDate(allBookings);
                case 2 -> filterBookingsByPerson(allBookings);
                case 3 -> filterBookingsByResource(allBookings);
                case 4 -> {
                }
                case 0 -> {
                    return;
                }
            }
        }
        adminBookingsMenu();
    }

    private void filterBookingsByDate(List<Booking> allBookings) {
        LocalDate dateRequired = ConsoleInput.dateInput();
        List<Booking> filteredBookings = allBookings.stream()
                .filter(booking -> booking.getDate().equals(dateRequired))
                .toList();

        if (filteredBookings.isEmpty())  {
            ConsoleOutput.print("Нет бронирований на дату: " + dateRequired);
        } else  {
            ConsoleOutput.print("Все бронирования на дату: " + dateRequired);
            ConsoleOutput.printList(filteredBookings);
        }
    }

    private void filterBookingsByPerson(List<Booking> allBookings) {
        Long maxId = personService.getAllPersons().stream()
                .mapToLong(Person::getId)
                .max()
                .orElseThrow(PersonNotFoundException::new);
        Long idRequired = ConsoleInput.intInput(InputType.ID, maxId);
        List<Booking> filteredBookings = allBookings.stream()
                .filter(booking -> booking.getPersonId().equals(idRequired)).toList();

        if (filteredBookings.isEmpty())  {
            ConsoleOutput.print("Нет бронирований для пользователя с ID: " + idRequired);
        } else  {
            ConsoleOutput.print("Все бронирования для пользователя с ID: " + idRequired);
            ConsoleOutput.printList(filteredBookings);
        }
    }

    private void filterBookingsByResource(List<Booking> allBookings) {
        Long maxId = resourceService.getAllResources().stream()
                .mapToLong(Resource::getId)
                .max()
                .orElseThrow(ResourceNotFoundException::new);
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

    private void adminBookingsMenu() {
        while (true) {
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_BOOKINGS_MENU);

            switch (choice) {
                case 1 -> {
                    Long maxId = bookingService.getAllBookings().stream()
                            .mapToLong(Booking::getId)
                            .max()
                            .orElse(-1L);
                    if (maxId  == -1L)   {
                        ConsoleOutput.print("Нет бронирований.");
                    } else {
                        Long idRequired = ConsoleInput.intInput(InputType.ID, maxId);
                        Booking deleted = bookingService.deleteById(idRequired);
                        ConsoleOutput.print("Удалено бронирование: " + deleted);
                        Util.notifyUser(personService.getPersonById(deleted.getPersonId()));
                    }
                }
                case 2 -> {
                    // TODO бронировать
                    // получить строку, валидировать, парсить
                    // запись сервис save()
                    // уведомить
                }
                case 3 -> {
// изменить бронь

                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void adminResourcesMenu() {
        // TODO
        while (true) {
            int choice = Util.askNumberForMenu(MenuValues.ADMIN_RESOURCES_MENU);
        }
    }

    private void userMainMenu(Person person) {
        // TODO
    }
}
