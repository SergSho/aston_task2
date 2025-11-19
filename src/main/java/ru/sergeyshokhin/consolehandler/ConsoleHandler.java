package ru.sergeyshokhin.consolehandler;

import java.util.Scanner;

public class ConsoleHandler {
    public static void write(String value) {
        System.out.println(value);
    }

    public static String read() {
        Scanner scan = new Scanner(System.in);
        String result = scan.nextLine().trim();
        if (result.equalsIgnoreCase("exit")) System.exit(0);
        return result;
    }
}
