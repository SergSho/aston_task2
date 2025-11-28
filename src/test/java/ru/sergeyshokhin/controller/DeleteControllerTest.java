package ru.sergeyshokhin.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class DeleteControllerTest extends ControllerTest{

    private final String RESPONSE_DELETED = "Запись в базе данных УДАЛЕНА.";
    private final String RESPONSE_NOT_FOUND_ID = "Запись в базе данных для введенного \"id\" НЕ НАЙДЕНА.";
    private final Controller controller = new DeleteController(validator, dao);

    @BeforeAll
    private static void init() {

        shortData = new StringBuilder()
                .append("25")
                .toString();

        longData = new StringBuilder()
                .append("025").append(SEPARATOR)
                .append("-25").append(SEPARATOR)
                .append("25")
                .toString();
    }

    @DisplayName("Получение параметра \"id\" и вызова метода \"dao.remove(id)\".")
    @ParameterizedTest
    @MethodSource("inputData")
    public void getIdAndInvokeDaoMethod_methodExecute(String input) {
        changeSystemIn(input);
        Mockito.doReturn(false).when(dao).remove(Mockito.anyInt());

        controller.execute();

        Mockito.verify(dao).remove(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(dao);
    }

    @DisplayName("Ответ, если запись в базе данных удалена")
    @Test
    public void responseIfUserDeleted_methodExecute() {
        changeSystemIn(shortData);
        changeSystemOut();
        Mockito.doReturn(true).when(dao).remove(Mockito.anyInt());

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length - 1];

        Mockito.verify(dao).remove(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(dao);
        Assertions.assertEquals (RESPONSE_DELETED, result);
    }

    @DisplayName("Ответ, если запись в базе данных НЕ удалена.")
    @Test
    public void responseIfUserNotDeleted_methodExecute() {
        changeSystemIn(shortData);
        changeSystemOut();
        Mockito.doReturn(false).when(dao).remove(Mockito.anyInt());

        controller.execute();

        String[] array = arrayFromBaos();
        int length = array.length;
        String result = array[length-1];

        Assertions.assertEquals(RESPONSE_NOT_FOUND_ID, result);
    }
}
