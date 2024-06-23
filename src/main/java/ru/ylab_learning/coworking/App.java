package ru.ylab_learning.coworking;

import ru.ylab_learning.coworking.controller.ConsoleMenuController;
import ru.ylab_learning.coworking.repository.BookingRepository;
import ru.ylab_learning.coworking.repository.PersonRepository;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.repository.impl.BookingRepositoryImpl;
import ru.ylab_learning.coworking.repository.impl.PersonRepositoryImpl;
import ru.ylab_learning.coworking.repository.impl.ResourceRepositoryImpl;
import ru.ylab_learning.coworking.service.BookingService;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.service.ResourceService;
import ru.ylab_learning.coworking.service.impl.BookingServiceImpl;
import ru.ylab_learning.coworking.service.impl.PersonServiceImpl;
import ru.ylab_learning.coworking.service.impl.ResourceServiceImpl;

/**
 * Точка входа.
 * Для целей тестирования приложения добавлены:
 * - администратор (admin/password)
 * - пользователь (user/password)
 * - 2 конференц-зала с эмуляцией занятости
 * - 5 рабочих мест с эмуляцией занятости
 */
public class App {
    public static void main(String[] args) {

        ConsoleMenuController console = initConsoleMenuController();

        console.startMenu();
    }

    private static ConsoleMenuController initConsoleMenuController() {
        PersonRepository personRepository = new PersonRepositoryImpl();
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        ResourceRepository resourceRepository = new ResourceRepositoryImpl();

        PersonService personService = new PersonServiceImpl(personRepository);
        ResourceService resourceService = new ResourceServiceImpl(resourceRepository);
        BookingService bookingService = new BookingServiceImpl(bookingRepository, personService, resourceService);

        return new ConsoleMenuController(personService, bookingService, resourceService);
    }

}
