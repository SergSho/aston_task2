package ru.sergeyshokhin.controller;


import ru.sergeyshokhin.consolehandler.ConsoleHandler;
import ru.sergeyshokhin.controller.validator.UserValidator;

public class DeleteController extends AbstractController {

    public DeleteController(UserValidator validator) {
        super(validator);
    }

    public void execute() {

        ConsoleHandler.write("Введите id для удаления \"User\".");
        String userId = prepareParameter("id");
        if (dao.remove(Integer.parseInt(userId))) {
            ConsoleHandler.write("Запись в базе данных УДАЛЕНА.");
        } else ConsoleHandler.write("Запись в базе данных для введенного \"id\" НЕ НАЙДЕНА.");

    }
}
