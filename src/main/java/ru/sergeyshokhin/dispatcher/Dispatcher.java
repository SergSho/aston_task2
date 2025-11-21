package ru.sergeyshokhin.dispatcher;

import lombok.extern.slf4j.Slf4j;
import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.Controller;

import java.util.Map;

@Slf4j
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
                    log.info("Выбрана команда: " + command + ".");
                    actions.get(command).execute();
                    break;
                }
                ConsoleHandler.write("Не известная команда. Повторно введите команду.");
                log.info ("Введена некорректная команда: " + command + ".");
            } while (true);
        } while (true);
    }
}
