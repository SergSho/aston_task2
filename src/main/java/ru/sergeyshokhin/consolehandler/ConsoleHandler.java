package ru.sergeyshokhin.consolehandler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleHandler {
    private static Scanner scan = new Scanner(System.in);
    public static void write(String value) {
        System.out.println(value);
    }

    public static String read() {

        String result = scan.nextLine().trim();
        if (result.equalsIgnoreCase("exit")) {
            log.info("Приложение остановлено.");
            System.exit(0);
        }
        return result;
    }
    public static void setScan(Scanner scanner){
       scan = scanner;
    }

}
