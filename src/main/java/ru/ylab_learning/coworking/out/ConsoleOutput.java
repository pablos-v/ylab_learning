package ru.ylab_learning.coworking.out;

import java.util.List;

public class ConsoleOutput {
    public static void print(String s){
        System.out.println(s);
    }
    public static <T> void printList(List<T> list){
        list.forEach(System.out::println);
    }

}
