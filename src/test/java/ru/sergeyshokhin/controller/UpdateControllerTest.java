package ru.sergeyshokhin.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;

import java.util.Optional;

public class UpdateControllerTest extends ControllerTest {
    private final Controller controller = new UpdateController(validator, dao);

    private static String allParamNull;

    private static User user;

    @BeforeAll
    private static void init() {
        user = new User(125, "Ivan", "example@mail.ru", 25);

        shortData = new StringBuilder()
                .append("125").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("Ivan").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("example@mail.ru").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("25")
                .toString();

        longData = new StringBuilder()
                .append("-125").append(SEPARATOR)
                .append("125").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("ivan").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("Ivan").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("example.mail.ru").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("example@mail.ru").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("033").append(SEPARATOR)
                .append("yes").append(SEPARATOR)
                .append("25")
                .toString();

        allParamNull = new StringBuilder()
                .append("125").append(SEPARATOR)
                .append("no").append(SEPARATOR)
                .append("no").append(SEPARATOR)
                .append("no").append(SEPARATOR)
                .toString();
    }

    @DisplayName("Создание объекта \"user\" и вызов метода \"dao.update(user)\".")
    @ParameterizedTest
    @MethodSource("inputData")
    public void createUserAndInvokeDaoMethod_methodExecute(String input) throws AppException {
        changeSystemIn(input);
        Optional<User> optionalUser = Optional.empty();
        Mockito.doReturn(optionalUser).when(dao).update(user);

        controller.execute();

        Mockito.verify(dao).update(user);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @DisplayName("Ответ, если \"user\" обновлен в базе данных.")
    @Test
    public void responseIfUserUpdated_methodExecute() throws AppException {
        changeSystemIn(shortData);
        changeSystemOut();
        Optional<User> optionalUser = Optional.of(user);
        Mockito.doReturn(optionalUser).when(dao).update(user);
        String expectedResult = "В базу данных введенная информация успешно ЗАПИСАНА.";

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result1 = array[length - 2];
        String result2 = array[length - 1];

        Mockito.verify(dao).update(user);
        Mockito.verifyNoMoreInteractions(dao);
        Assertions.assertEquals(expectedResult, result1);
        Assertions.assertEquals(user.toString(), result2);
    }

    @DisplayName("Ответ, если \"user\"  НЕ обновлен (не найден) в базе данных.")
    @Test
    public void responseIfUserNotUpdated_methodExecute() throws AppException {
        changeSystemIn(shortData);
        changeSystemOut();
        Optional<User> optionalUser = Optional.empty();
        Mockito.doReturn(optionalUser).when(dao).update(user);
        String expectedResult = """
                Запись в базе данных НЕ ОБНОВЛЕНА.
                Неверно указан id.""";

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length - 1];

        Assertions.assertEquals(expectedResult, result);
    }

    @DisplayName("Ответ, если \"user\"  НЕ обновлен (ошибка уникальности) в базе данных.")
    @Test
    public void responseIfUserNotUpdated_Exception_methodExecute() throws AppException {
        changeSystemIn(shortData);
        changeSystemOut();
        Mockito.doThrow(new AppException("Нарушена уникальность. Данный email уже зарегистрирован.", null))
                .when(dao).update(user);
        String expectedResult = "Нарушена уникальность. Данный email уже зарегистрирован.";

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length - 1];

        Assertions.assertEquals(expectedResult, result);
    }

    @DisplayName("Создание объекта \"user\" и вызов метода \"dao.update(user)\".")
    @Test
    public void createUserAndNotInvokeDaoMethod_methodExecute() {
        changeSystemIn(allParamNull);
        changeSystemOut();
        String expectedResult = "Обновление не требуется.";

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length - 1];

        Mockito.verifyNoInteractions(dao);
        Assertions.assertEquals(expectedResult, result);
    }
}
