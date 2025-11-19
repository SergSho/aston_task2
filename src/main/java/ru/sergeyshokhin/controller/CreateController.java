package ru.sergeyshokhin.controller;

import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;

import java.util.HashMap;
import java.util.Map;

public class CreateController extends AbstractController {

    public CreateController(UserValidator validator) {
        super(validator);
    }

    public void execute() {

        Map<String, String> data = new HashMap<>(3) {{
            put("name", null);
            put("email", null);
            put("age", null);
        }};

        ConsoleHandler.write("Введите данные для создания \"User\".");
        for (String key : data.keySet()) {
            String value = prepareParameter(key);
            data.put(key, value);
        }
        User user = new User(
                data.get("name"),
                data.get("email").toLowerCase(),
                Integer.parseInt(data.get("age")));

        try {
            User newUser = dao.create(user);
            ConsoleHandler.write("Запись в базе данных СОЗДАНА.");
            ConsoleHandler.write(newUser.toString());
        } catch (AppException e) {
            ConsoleHandler.write(e.getMessage());
        }

    }
}
