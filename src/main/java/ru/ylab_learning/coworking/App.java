package ru.ylab_learning.coworking;

import ru.ylab_learning.coworking.controller.ConsoleMenuController;
import ru.ylab_learning.coworking.repository.BookingRepository;
import ru.ylab_learning.coworking.repository.PersonRepository;
import ru.ylab_learning.coworking.repository.ResourceRepository;
import ru.ylab_learning.coworking.service.BookingService;
import ru.ylab_learning.coworking.service.PersonService;
import ru.ylab_learning.coworking.service.ResourceService;
import ru.ylab_learning.coworking.service.impl.BookingServiceImpl;
import ru.ylab_learning.coworking.service.impl.PersonServiceImpl;
import ru.ylab_learning.coworking.service.impl.ResourceServiceImpl;

public class App {
    public static void main(String[] args) {

        PersonRepository personRepository   = new PersonRepositoryImpl();
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        ResourceRepository resourceRepository  = new ResourceRepositoryImpl();

        PersonService personService  = new PersonServiceImpl(personRepository);
        BookingService bookingService  = new BookingServiceImpl(bookingRepository);
        ResourceService resourceService   = new ResourceServiceImpl(resourceRepository);
        
        ConsoleMenuController console = new ConsoleMenuController(personService, bookingService, resourceService);
        
        // Для целей тестирования приложения добавлены:
//        - администратор (adm/password)
//                - пользователь (user/password)
        // - 2 конференц-зала (большой и малый) с эмуляцией занятости
        // - 10 рабочих мест (5 из них типа Комфорт+) с эмуляцией занятости
        
        console.startMenu();

    }
}
