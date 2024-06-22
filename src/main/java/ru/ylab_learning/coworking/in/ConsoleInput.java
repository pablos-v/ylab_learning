package ru.ylab_learning.coworking.in;

import lombok.RequiredArgsConstructor;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.out.ConsoleOutput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Класс для работы с консольным потоком ввода
 */
@RequiredArgsConstructor
public class ConsoleInput {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Метод ввода строк в консоль, с отображением вводных данных
     *
     * @param type тип вводных данных
     * @return валидный массив строк
     */
    public static String[] stringsInput(InputType type) {
        while (true) {
            ConsoleOutput.print(type.toString());

            String input = scanner.nextLine();
            String[] data = input.split(" ");

            if (data.length > type.getNumberOfWordsExpected()
                    || (!type.equals(InputType.RESOURCE) && data.length <  type.getNumberOfWordsExpected()))  {
                ConsoleOutput.print("Неправильный формат ввода. Попробуйте снова.");
            } else {
                return data;
            }
        }
    }

    /**
     * Метод ввода чисел в консоль, с отображением вводных данных
     *
     * @param type тип вводных данных
     * @param maxNumber для первичной валидации
     * @return валидное число
     */
    public static Long intInput(InputType type, Long maxNumber) {
        while (true) {
            ConsoleOutput.print(type.toString());
            if (scanner.hasNextInt()) {
                int selection = scanner.nextInt();
                if (!(selection < 0 || selection > maxNumber)) {
                    scanner.nextLine();
                    return (long) selection;
                }
            } else {
                ConsoleOutput.print("Неправильный формат ввода. Попробуйте снова.");
            }
        }
    }

    /**
     * Метод ввода даты в консоль, проверяет и приводит к формату LocalDate
     *
     * @return валидная дата
     */
    public static LocalDate dateInput() {
        while (true) {
            ConsoleOutput.print("Введите дату в формате ДД-ММ-ГГГГ:");
            String inputDate = scanner.nextLine();
            try {
                return LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                ConsoleOutput.print("Неправильный формат ввода. Попробуйте снова.");
            }
        }
    }
}