package ru.sergeyshokhin.controller;


import lombok.extern.slf4j.Slf4j;
import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dao.UserDao;

import java.util.Scanner;

@Slf4j
public class DeleteController extends AbstractController {

    public DeleteController(UserValidator validator, UserDao dao) {
        super(validator, dao);
    }

    public void execute() {

        log.info("Новый запрос на удаление объекта из базы данных.");
        ConsoleHandler.setScan(new Scanner(System.in));
        ConsoleHandler.write("Введите id для удаления \"User\".");
        String userId = prepareParameter("id");
        log.info("\"id\" объекта, удаляемого из базы данных: " + userId);
        if (dao.remove(Integer.parseInt(userId))) {
            log.info("Объект c параметром \"id\" = " + userId +" удален из базы данных.");
            ConsoleHandler.write("Запись в базе данных УДАЛЕНА.");

        } else {
            log.warn("Объект c параметром \"id\" = " + userId +" не найден в базе данных.");
            ConsoleHandler.write("Запись в базе данных для введенного \"id\" НЕ НАЙДЕНА.");
        }

    }
}
