package ru.sergeyshokhin.controller;


import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpdateController extends AbstractController {

    public UpdateController(UserValidator validator) {
        super(validator);
    }

    public void execute() {

        Map<String, String> data = new HashMap<>(3) {{
            put("name", null);
            put("email", null);
            put("age", null);
        }};

        ConsoleHandler.write("Введите id для обновляемого \"User\"");
        String userId = prepareParameter("id");

        ConsoleHandler.write("Введите данные для обновления \"User\".");
        for (String key : data.keySet()) {
            if (!shouldUpdate(key)) continue;
            String value = prepareParameter(key);
            data.put(key, value);
        }

        if (!isRequiredUpdate(data)) {
            ConsoleHandler.write("Обновление не требуется.");
            return;
        }

        int age = data.get("age") == null ? 0 : Integer.parseInt(data.get("age"));
        User user = new User(
                Integer.parseInt(userId),
                data.get("name"),
                data.get("email"),
                age);

        try {
            Optional<User> optionalUser = dao.update(user);
            if (optionalUser.isPresent()) {
                ConsoleHandler.write("В базу данных введенная информация успешно ЗАПИСАНА.");
                ConsoleHandler.write(optionalUser.get().toString());
            } else ConsoleHandler.write("""
                    Запись в базе данных НЕ ОБНОВЛЕНА.
                    Неверно указан id.
                    """);

        } catch (AppException e) {
            ConsoleHandler.write(e.getMessage());
        }
    }

    private boolean shouldUpdate(String parameter) {
        ConsoleHandler.write("Следует ли обновить параметр \"" + parameter + "\"?");
        ConsoleHandler.write("""
                Введите "yes", если требуется.
                Введите "no", если не требуется.
                """);
        do {
            String command = ConsoleHandler.read();
            if (command.equalsIgnoreCase("yes")) return true;
            else if (command.equalsIgnoreCase("no")) return false;
            else ConsoleHandler.write("Неверно введена команда. Повторите.");
        } while (true);
    }

    private boolean isRequiredUpdate(Map<String, String> data) {
        for (String key : data.keySet()) {
            if (data.get(key) != null) return true;
        }
        return false;
    }

}
