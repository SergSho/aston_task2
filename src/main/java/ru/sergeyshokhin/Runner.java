package ru.sergeyshokhin;

import lombok.extern.slf4j.Slf4j;
import ru.sergeyshokhin.controller.*;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dao.UserDao;
import ru.sergeyshokhin.dispatcher.Dispatcher;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        UserValidator validator = new UserValidator();
        UserDao dao = UserDao.getINSTANCE();
        Controller create = new CreateController(validator, dao);
        Controller get = new GetController(validator, dao);
        Controller delete = new DeleteController(validator, dao);
        Controller update = new UpdateController(validator, dao);
        Map<String, Controller> actions = new HashMap<>() {{
           put("create", create);
            put("get", get);
            put("delete", delete);
            put("update", update);
        }};
        Dispatcher dispatcher =new Dispatcher(actions);
        log.info("Приложение запущено.");
        dispatcher.dispatch();
    }
}
