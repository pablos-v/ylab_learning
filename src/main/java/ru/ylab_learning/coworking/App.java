package ru.ylab_learning.coworking;

import ru.ylab_learning.coworking.controller.ConsoleMenuController;

/**
 * Точка входа.
 */
public class App {
    public static void main(String[] args) {

        ConsoleMenuController console = ConsoleMenuController.build();

        console.startMenu();
    }
}
