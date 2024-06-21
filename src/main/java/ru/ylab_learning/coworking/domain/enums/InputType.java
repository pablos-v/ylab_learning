package ru.ylab_learning.coworking.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Типы ввода данных, с указанием максимального кол-ва слов в строке.
 * Нужны для корректного переиспользования модулей ввода в классе ConsoleInput.
 */
@Getter
@RequiredArgsConstructor
public enum InputType {
    /**
     * Введите логин и пароль через пробел:
     */
    AUTH(2){
        @Override
        public String toString() {
            return "Введите логин и пароль через пробел: ";
        }
    },
    /**
     * Образец ввода: user password Юля youlia@example.com <br/>
     * Укажите логин, пароль, имя и email через пробел:
     */
    REGISTER(4){
        @Override
        public String toString() {
            return """
                    Образец ввода: user password Юля youlia@example.com
                    Укажите логин, пароль, имя и email через пробел:
                    """;
        }
    },
    /**
     * Выберите пункт меню:
     */
    MENU_NUMBER(1){
        @Override
        public String toString() {
            return "Выберите пункт меню: ";
        }
    },
    /**
     * Укажите ID:
     */
    ID(1){
        @Override
        public String toString() {
            return "Укажите ID: ";
        }
    },
    /**
     * Образец ввода: 3 15 22-06-2024 15 17 <br/>
     * Введите ID ресурса, ID пользователя, дату, часы начала <br/>
     * и окончания, разделяя пробелами:
     */
    ADMIN_BOOKING(5){
        @Override
        public String toString() {
            return """
                    Образец ввода: 3 15 22-06-2024 15 17
                    Введите ID ресурса, ID пользователя, дату, часы начала
                    и окончания, разделяя пробелами:
                    """;
        }
    },
    /**
     * Образец ввода: 3 22-06-2024 15 17 <br/>
     * Введите ID объекта, дату, часы начала и окончания, разделяя пробелами:
     */
    USER_BOOKING(4){
        @Override
        public String toString() {
            return """
                    Образец ввода: 3 22-06-2024 15 17
                    Введите ID объекта, дату, часы начала и окончания, разделяя пробелами:
                    """;
        }
    },
    /**
     * Образец ввода: place 250 comfort+ with printer <br/>
     * Введите тип ресурса (room или place), стоимость часа аренды <br/>
     * и описание объекта (до 10 слов), разделяя пробелами:
     */
    RESOURCE(12){
        @Override
        public String toString() {
            return """
                    Образец ввода: place 250 comfort+ with printer
                    Введите тип ресурса (room или place), стоимость часа аренды
                    и описание объекта (до 10 слов), разделяя пробелами:
                    """;
        }
    };

    private final int numberOfWordsExpected;

}
