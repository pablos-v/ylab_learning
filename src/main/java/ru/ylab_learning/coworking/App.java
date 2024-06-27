package ru.ylab_learning.coworking;

import ru.ylab_learning.coworking.controller.ConsoleMenuController;
import ru.ylab_learning.coworking.service.DatabaseInitiationService;

/**
 * Точка входа.
 */
public class App {
    public static void main(String[] args) {

        DatabaseInitiationService DBInitializer = new DatabaseInitiationService();
        DBInitializer.InitiateDatabaseWithLiquibase();

        ConsoleMenuController console = ConsoleMenuController.build(DBInitializer);
        console.startMenu();
    }
}

// TODO : Добавить тесты
// TODO : Докер
// TODO : убрать H2
