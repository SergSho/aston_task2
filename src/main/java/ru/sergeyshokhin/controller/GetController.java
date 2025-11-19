package ru.sergeyshokhin.controller;

import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.entity.User;

import java.util.Optional;

public class GetController extends AbstractController {

    public GetController(UserValidator validator) {
        super(validator);
    }

    public void execute() {

        ConsoleHandler.write("Введите id для получения \"User\".");
        String userId = prepareParameter("id");

        Optional<User> user = dao.get(Integer.parseInt(userId));
        if (user.isPresent()) {
            ConsoleHandler.write("Запись в базе данных НАЙДЕНА.");
            ConsoleHandler.write(user.get().toString());
        } else ConsoleHandler.write("Запись в базе данных для введенного \"id\" НЕ НАЙДЕНА.");
    }
}
