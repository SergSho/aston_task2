package ru.sergeyshokhin.controller;


import lombok.extern.slf4j.Slf4j;

import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class CreateController extends AbstractController {

    public CreateController(UserValidator validator) {
        super(validator);
    }

    public void execute() {
        log.info("Запрос на добавление объекта в базу данных");
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
                data.get("email"),
                Integer.parseInt(data.get("age")));

        log.info("Параметры объекта, добавляемого в базу данных: " + user);
        try {
            User newUser = dao.create(user);
            log.info("Объект записан в базу данных со следующими параметрами: " + user);
            ConsoleHandler.write("Запись в базе данных СОЗДАНА.");
            ConsoleHandler.write(newUser.toString());
        } catch (AppException e) {
            ConsoleHandler.write(e.getMessage());
        }

    }
}
