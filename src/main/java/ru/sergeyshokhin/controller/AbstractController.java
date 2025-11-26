package ru.sergeyshokhin.controller;


import lombok.extern.slf4j.Slf4j;
import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dao.UserDao;

import java.util.Scanner;

@Slf4j
public abstract class AbstractController implements Controller {
    protected final UserDao dao;
    private final  UserValidator validator;

    public AbstractController(UserValidator validator, UserDao dao) {
                this.validator = validator;
        this.dao = dao;
    }

    String prepareParameter(String parameter, Scanner scanner) {
        String result;
        do {
            ConsoleHandler.write("Введите параметр: " + parameter + ".");
            result = scanner.nextLine();
            if (validator.validate(result, parameter)) {
                log.info("Введен параметр: " + parameter + " = " +  result + ".");
                break;
            }

            ConsoleHandler.write("Введены не валидные данные.");
            log.info("Введен параметр c невалидным значением: " + parameter + " = " +  result + ".");
        } while (true);
        return result;
    }

    public abstract void execute();
}
