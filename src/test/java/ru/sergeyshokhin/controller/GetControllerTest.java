package ru.sergeyshokhin.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.sergeyshokhin.entity.User;

import java.util.Optional;

public class GetControllerTest extends ControllerTest {

    private final String RESPONSE_FOUND = "Запись в базе данных НАЙДЕНА.";
    private final String RESPONSE_NOT_FOUND_ID = "Запись в базе данных для введенного \"id\" НЕ НАЙДЕНА.";
    private final Controller controller = new GetController(validator, dao);

    private static User user;

    @BeforeAll
    private static void init() {
        user = new User("Ivan", "example@mail.ru", 25);

        shortData = new StringBuilder()
                .append("25")
                .toString();

        longData = new StringBuilder()
                .append("025").append(SEPARATOR)
                .append("-25").append(SEPARATOR)
                .append("25")
                .toString();
    }

    @DisplayName("Получение параметра \"id\" и вызова метода \"dao.get(id)\".")
    @ParameterizedTest
    @MethodSource("inputData")
    public void getIdAndInvokeDaoMethod_methodExecute(String input) {
        changeSystemIn(input);
        Optional<User> optionalUser = Optional.empty();
        Mockito.doReturn(optionalUser).when(dao).get(Mockito.anyInt());

        controller.execute();

        Mockito.verify(dao).get(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(dao);
    }

    @DisplayName("Ответ, если запись в базе данных найдена")
    @Test
    public void responseIfUserFound_methodExecute() {
        changeSystemIn(shortData);
        changeSystemOut();
        Optional<User> optionalUser = Optional.of(user);
        Mockito.doReturn(optionalUser).when(dao).get(Mockito.anyInt());

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result1 = array[length - 2];
        String result2 = array[length - 1];

        Mockito.verify(dao).get(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(dao);
        Assertions.assertEquals (RESPONSE_FOUND, result1);
        Assertions.assertEquals (user.toString(), result2);
    }

    @DisplayName("Ответ, если запись в базе данных НЕ найдена.")
    @Test
    public void responseIfUserNotFound_methodExecute() {
        changeSystemIn(shortData);
        changeSystemOut();
        Optional<User> optionalUser = Optional.empty();
        Mockito.doReturn(optionalUser).when(dao).get(Mockito.anyInt());

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length-1];

        Assertions.assertEquals(RESPONSE_NOT_FOUND_ID, result);
    }
}
