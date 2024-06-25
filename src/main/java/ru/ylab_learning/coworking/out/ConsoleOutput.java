package ru.ylab_learning.coworking.out;

import java.util.List;

/**
 * Класс для работы с консольным потоком вывода
 */
public class ConsoleOutput {
    /**
     * Выводит в консоль строку
     * @param s строка
     */
    public static void print(String s){
        System.out.println(s);
    }

    /**
     * Выводит в консоль список условного типа
     * @param list список условного типа
     * @param <T> условный тип
     */
    public static <T> void printList(List<T> list){
        list.forEach(System.out::println);
    }

}
