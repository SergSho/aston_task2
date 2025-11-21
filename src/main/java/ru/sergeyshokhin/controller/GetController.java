package ru.sergeyshokhin.controller;

import lombok.extern.slf4j.Slf4j;
import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.entity.User;

import java.util.Optional;

@Slf4j
public class GetController extends AbstractController {

    public GetController(UserValidator validator) {
        super(validator);
    }

    public void execute() {

        log.info("Новый запрос на получение объекта из базы данных.");
        ConsoleHandler.write("Введите id для получения \"User\".");
        String userId = prepareParameter("id");
        log.info("\"id\" объекта, получаемого из базы данных: " + userId);
        Optional<User> user = dao.get(Integer.parseInt(userId));
        if (user.isPresent()) {
            log.info("Получен объект из базы данных: " + user.get());
            ConsoleHandler.write("Запись в базе данных НАЙДЕНА.");
            ConsoleHandler.write(user.get().toString());

        } else {
            log.warn("Объект c параметром \"id\" = " + userId +" не найден в базе данных.");
            ConsoleHandler.write("Запись в базе данных для введенного \"id\" НЕ НАЙДЕНА.");
        }
    }
}
