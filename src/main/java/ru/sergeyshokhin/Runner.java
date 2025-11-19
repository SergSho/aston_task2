package ru.sergeyshokhin;

import ru.sergeyshokhin.controller.*;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dispatcher.Dispatcher;

import java.util.HashMap;
import java.util.Map;

public class Runner {
    public static void main(String[] args) {
        UserValidator validator = new UserValidator();
        Controller create = new CreateController(validator);
        Controller get = new GetController(validator);
        Controller delete = new DeleteController(validator);
        Controller update = new UpdateController(validator);
        Map<String, Controller> actions = new HashMap<>() {{
           put("create", create);
            put("get", get);
            put("delete", delete);
            put("update", update);
        }};
        Dispatcher dispatcher =new Dispatcher(actions);
        dispatcher.dispatch();
    }
}
