package ru.sergeyshokhin.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;


public class CreateControllerTest extends ControllerTest {
    private final Controller controller = new CreateController(validator, dao);

    private static User user;

    @BeforeAll
    private static void init (){
        user = new User("Ivan", "example@mail.ru", 25);

        shortData = new StringBuilder()
                .append("Ivan").append(SEPARATOR)
                .append("example@mail.ru").append(SEPARATOR)
                .append("25")
                .toString();

        longData = new StringBuilder()
                .append("ivan").append(SEPARATOR)
                .append("Ivan").append(SEPARATOR)
                .append("example.mail.ru").append(SEPARATOR)
                .append("example@mail.ru").append(SEPARATOR)
                .append("033").append(SEPARATOR)
                .append("25")
                .toString();
    }

    @DisplayName("Создание объекта \"user\" и вызов метода \"dao.create(user)\".")
    @ParameterizedTest
    @MethodSource("inputData")
    public void createUserAndInvokeDaoMethod_methodExecute(String input) throws AppException {
        changeSystemIn(input);
        Mockito.doReturn(user).when(dao).create(user);

        controller.execute();

        Mockito.verify(dao).create(user);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @DisplayName("Ответ, если \"user\" добавлен в базу данных.")
    @Test
    public void responseIfUserAddedToDB_methodExecute() throws AppException {
        changeSystemIn(shortData);
        changeSystemOut();
        Mockito.doReturn(user).when(dao).create(user);

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result1 = array[length - 2];
        String result2 = array[length - 1];

        Mockito.verify(dao).create(user);
        Mockito.verifyNoMoreInteractions(dao);
        Assertions.assertEquals("Запись в базе данных СОЗДАНА.", result1);
        Assertions.assertEquals(user.toString(), result2);
    }

    @DisplayName("Ответ, если \"user\"  НЕ добавлен в базу данных.")
    @Test
    public void responseIfUserNotAddedToDB_methodExecute() throws AppException {
        changeSystemIn(shortData);
        changeSystemOut();
        Mockito.doThrow(new AppException("Нарушена уникальность. Данный email уже зарегистрирован.", null))
                    .when(dao).create(user);

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length - 1];

        Assertions.assertEquals("Нарушена уникальность. Данный email уже зарегистрирован.", result);
    }
}
