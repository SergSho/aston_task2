package ru.sergeyshokhin.controller;


import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dao.UserDao;

public abstract class AbstractController implements Controller {
    UserDao dao = UserDao.getINSTANCE();
    private UserValidator validator;

    public AbstractController(UserValidator validator) {
        this.validator = validator;
    }

    String prepareParameter(String parameter) {
        String result;
        do {
            ConsoleHandler.write("Введите параметр: " + parameter + ".");
            result = ConsoleHandler.read();
            if (validator.validate(result, parameter)) break;
            ConsoleHandler.write("Введены не валидные данные.");
        } while (true);
        return result;
    }

    public abstract void execute();
}
