package ru.sergeyshokhin.dispatcher;

import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.Controller;

import java.util.Map;

public class Dispatcher {
    private final Map<String, Controller> actions;

    public Dispatcher(Map<String, Controller> actions) {
        this.actions = actions;
    }

    public void dispatch() {
        ConsoleHandler.write("Приветствуем Вас в приложении.");
        do {
            ConsoleHandler.write("""
                    Вы в главном меню.
                    Введите команду:
                    "create"  - создать экземпляр "User",
                    "delete"  - удалить экземпляр "User",
                    "update"  - обновить экземпляр "User",
                    "get"  - получить экземпляр "User",
                    "exit"  - выход из приложения.
                    """);
            do {
                String command = ConsoleHandler.read().toLowerCase();
                if (actions.containsKey(command)) {
                    actions.get(command).execute();
                    break;
                }
                ConsoleHandler.write("Не известная команда. Повторно введите команду.");
            } while (true);
        } while (true);
    }
}
