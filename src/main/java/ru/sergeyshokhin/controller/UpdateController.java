package ru.sergeyshokhin.controller;


import lombok.extern.slf4j.Slf4j;
import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dao.UserDao;
import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class UpdateController extends AbstractController {

    public UpdateController(UserValidator validator, UserDao dao) {
        super(validator, dao);
    }

    public void execute() {

        log.info("Новый запрос на обновление объекта в базе данных.");
        Scanner scanner = new Scanner(System.in);
        Map<String, String> data = new HashMap<>(3) {{
            put("name", null);
            put("email", null);
            put("age", null);
        }};

        ConsoleHandler.write("Введите id для обновляемого \"User\"");
        String userId = prepareParameter("id", scanner);

        ConsoleHandler.write("Введите данные для обновления \"User\".");
        for (String key : data.keySet()) {
            if (!shouldUpdate(key, scanner)) continue;
            String value = prepareParameter(key, scanner);
            data.put(key, value);
        }

        if (!isRequiredUpdate(data)) {
            log.info("Параметры для обновления объекта в базе данных не введены.");
            ConsoleHandler.write("Обновление не требуется.");
            return;
        }

        int age = data.get("age") == null ? 0 : Integer.parseInt(data.get("age"));
        User user = new User(
                Integer.parseInt(userId),
                data.get("name"),
                data.get("email"),
                age);
        log.info("Параметры объекта, обновляемого в базе данных: " + user);
        try {
            Optional<User> optionalUser = dao.update(user);
            if (optionalUser.isPresent()) {
                log.info("Объект в базе данных обновлен. Объект имеет следующие параметры: " + optionalUser.get() + ".");
                ConsoleHandler.write("В базу данных введенная информация успешно ЗАПИСАНА.");
                ConsoleHandler.write(optionalUser.get().toString());
            } else {
                log.warn("Введен отсутствующий в базе данных \"id\" = " + user.getId());
                ConsoleHandler.write("""
                    Запись в базе данных НЕ ОБНОВЛЕНА.
                    Неверно указан id.
                    """);
            }

        } catch (AppException e) {
            log.error("AppException: " + e.getMessage());
            log.info("Введены параметры: "+ user);
            ConsoleHandler.write(e.getMessage());
        }
    }

    private boolean shouldUpdate(String parameter, Scanner scanner) {
        ConsoleHandler.write("Следует ли обновить параметр \"" + parameter + "\"?");
        ConsoleHandler.write("""
                Введите "yes", если требуется.
                Введите "no", если не требуется.
                """);
        do {
            String command = scanner.nextLine();
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
