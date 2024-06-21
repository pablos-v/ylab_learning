package ru.ylab_learning.coworking.in;

import lombok.RequiredArgsConstructor;
import ru.ylab_learning.coworking.domain.enums.InputType;
import ru.ylab_learning.coworking.out.ConsoleOutput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleInput {
    /**
     * ограниечение количества вводимых символов
     */
//    private static final int MAX_SYMBOLS = 50;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * ЛОгика ввода строк в консоль, в зависимости от типа вводных данных
     *
     * @param type тип вводных данных
     * @return отвалидированный массив строк
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
     * Циклично просит ввести число, сообщая message
     * проверяет на соответствие максимальному порогу
     *
     * @param message   подставляемое сообщение
     * @param maxNumber максимальный порог
     * @return корректное введённое число int
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
     * получает ввод даты, проверяет и приводит к формату LocalDate
     *
     * @return LocalDate date
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
