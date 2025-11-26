package ru.sergeyshokhin.controller;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.sergeyshokhin.controller.validator.UserValidator;
import ru.sergeyshokhin.dao.UserDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ControllerTest{

    protected static String shortData;
    protected static String longData;

    protected static final String SEPARATOR = System.lineSeparator();
    protected final UserValidator validator = new UserValidator();

    private ByteArrayOutputStream baos;

    @Mock
    protected UserDao dao = Mockito.mock(UserDao.class);

    private final InputStream INPUT = System.in;
	private final PrintStream OUT= System.out;

    protected static Stream<String> inputData () {
        List<String> list = new ArrayList<>();
        list.add(shortData);
        list.add(longData);
        return list.stream();
    }


    @AfterEach
    public void afterEach (){
        System.setIn(INPUT);
        System.setOut(OUT);

    }

    protected void changeSystemOut() {
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

    }

    protected String [] arrayFromBaos() {
        return baos.toString().trim().split(SEPARATOR);

    }

    protected void changeSystemIn(String value) {
        System.setIn(new ByteArrayInputStream(value.getBytes()));
    }
}
