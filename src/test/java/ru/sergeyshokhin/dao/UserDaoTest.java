package ru.sergeyshokhin.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.sergeyshokhin.entity.User;
import ru.sergeyshokhin.exception.AppException;
import ru.sergeyshokhin.util.AppUtil;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;


public class UserDaoTest {
    private static final String NAME = "Alex";
    private static final String NAME_FOR_UPDATE = "Alexandr";

    private static final String EMAIL = "example%d@yandex.ru";
    private static final String EMAIL_FOR_CREATE = "example1000@yandex.ru";
    private static final String EMAIL_FOR_UPDATE = "example2000@yandex.ru";

    private static final String REPEATABLE_EMAIL = "example1@yandex.ru";
    private static final int AGE = 30;
    private static final int AGE_FOR_UPDATE = 25;

    private static final String EXCEPTION_MESSAGE = "Нарушена уникальность. Данный email уже зарегистрирован.";
    private static final String EXCEPTION_TYPE = "ConstraintViolationException";

    private static final Integer ABSENT_ID_10 = 10;
    private static final Integer ABSENT_ID_11 = 11;
    private static final Integer EXIST_USER_ID_FOR_STREAM = 5;

    private static final int USERS = 5;

    private static SessionFactory sessionFactory = AppUtil.getSessionFactory();

    private static UserDao dao;

    @BeforeAll
    public static void init() {
        dao = UserDao.getINSTANCE();
        sessionFactory = dao.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (int i = 1; i <= USERS; i++) {
                session.persist(new User(NAME, EMAIL.formatted(i), AGE));
            }
        }
    }

    @AfterAll
    public static void close() {
        sessionFactory.close();
    }

    private static Stream<Integer> streamOfAbsentId() {
        return Stream.of(ABSENT_ID_10, ABSENT_ID_11);
    }

    private static Stream<User> streamOfUsers() {
        return Stream.of(new User(EXIST_USER_ID_FOR_STREAM, NAME_FOR_UPDATE, null, 0),
                new User(EXIST_USER_ID_FOR_STREAM, null, EMAIL_FOR_UPDATE, 0),
                new User(EXIST_USER_ID_FOR_STREAM, null, null, AGE_FOR_UPDATE));
    }


    @DisplayName("Вызов метода \"get(id)\" с успешным получением из базы данных.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void resultForIdIfExists_get(Integer id) {
        Optional<User> result = dao.get(id);

        User user = result.get();

        Assertions.assertEquals(id, user.getId());
        Assertions.assertEquals(NAME, user.getName());
        Assertions.assertEquals(EMAIL.formatted(id), user.getEmail());
        Assertions.assertEquals(AGE, user.getAge());

        // на стыке суток (time: 00:00)  - может дать неверный результат!!!
        Assertions.assertEquals(LocalDate.now(), user.getCreated_at());
    }


    @DisplayName("Вызов метода \"get(id)\" при отсутствии id в базе данных.")
    @ParameterizedTest
    @MethodSource("streamOfAbsentId")
    public void resultForIdIfNotExists_get(Integer id) {

        Optional<User> expected = Optional.empty();

        Optional<User> result = dao.get(id);

        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Вызов метода \"remove(id)\" с успешным удалением из базы данных.")
    @ParameterizedTest
    @ValueSource(ints = {3, 4})
    public void resultForIdIfExists_remove(Integer id) {

        User user;

        boolean result = dao.remove(id);

        try (Session session = sessionFactory.openSession()) {
            user = session.find(User.class, id);
        }
        boolean expected = (user == null);
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Вызов метода \"remove(id)\" при отсутствии id в базе данных.")
    @ParameterizedTest
    @MethodSource("streamOfAbsentId")
    public void resultForIdIfNotExists_remove(Integer id) {

        boolean result = dao.remove(id);

        Assertions.assertFalse(result);
    }

    @DisplayName("Вызов метода \"create(user)\" с успешным сохранением в базу данных.")
    @Test
    public void resultForAddedUser_create() throws AppException {

        User user = new User(NAME, EMAIL_FOR_CREATE, AGE);

        User resultUser = dao.create(user);

        Assertions.assertNotNull(resultUser.getId());
        Assertions.assertTrue(resultUser.getId() > USERS);
        Assertions.assertEquals(NAME, resultUser.getName());
        Assertions.assertEquals(EMAIL_FOR_CREATE, resultUser.getEmail());
        Assertions.assertEquals(AGE, resultUser.getAge());

        // на стыке суток (time: 00:00)  - даст неверный результат!!!
        Assertions.assertEquals(LocalDate.now(), resultUser.getCreated_at());
    }

    @DisplayName("Вызов метода \"create(user)\" без сохранения (ошибка базы данных).")
    @Test
    public void throwsAppException_create() {

        User user = new User(NAME, REPEATABLE_EMAIL, AGE);

        Exception e = Assertions.assertThrows(AppException.class, () -> dao.create(user));
        Assertions.assertEquals(EXCEPTION_TYPE, e.getCause().getClass().getSimpleName());
        Assertions.assertEquals(EXCEPTION_MESSAGE, e.getMessage());
    }

    @DisplayName("Вызов метода \"update(user)\" с успешным обновлением в базе данных.")
    @ParameterizedTest
    @MethodSource("streamOfUsers")
    public void resultForIdIfExists_update(User user) throws AppException {

        String resultName;
        String resultEMAIL;
        int resultAge;
        if (user.getName() != null) {
            resultName = NAME_FOR_UPDATE;
            resultEMAIL = EMAIL.formatted(EXIST_USER_ID_FOR_STREAM);
            resultAge = AGE;
        } else if (user.getEmail() != null) {
            resultName = NAME_FOR_UPDATE;
            resultEMAIL = EMAIL_FOR_UPDATE;
            resultAge = AGE;
        } else {
            resultName = NAME_FOR_UPDATE;
            resultEMAIL = EMAIL_FOR_UPDATE;
            resultAge = AGE_FOR_UPDATE;
        }

        Optional<User> result = dao.update(user);

        user = result.get();

        Assertions.assertNotNull(user);
        Assertions.assertEquals(EXIST_USER_ID_FOR_STREAM, user.getId());
        Assertions.assertEquals(resultName, user.getName());
        Assertions.assertEquals(resultEMAIL, user.getEmail());
        Assertions.assertEquals(resultAge, user.getAge());
    }



    @DisplayName("Вызов метода \"update(user)\" без обновления (ошибка базы данных).")
    @Test
    public void throwsAppException_update() {

        // Any id except the number specified in the email
        User user = new User(EXIST_USER_ID_FOR_STREAM, NAME_FOR_UPDATE, REPEATABLE_EMAIL, AGE_FOR_UPDATE);

        Exception e = Assertions.assertThrows(AppException.class, () -> dao.update(user));
        Assertions.assertEquals(EXCEPTION_TYPE, e.getCause().getClass().getSimpleName());
        Assertions.assertEquals(EXCEPTION_MESSAGE, e.getMessage());
    }

    @DisplayName("Вызов метода \"update(user)\"  при отсутствии id в базе данных.")
    @ParameterizedTest
    @MethodSource("streamOfAbsentId")
    public void resultForIdIfNotExists_update(Integer id) throws AppException {

        User user = new User(id, NAME_FOR_UPDATE, EMAIL_FOR_UPDATE, AGE_FOR_UPDATE);
        Optional<User> expected = Optional.empty();

        Optional<User> result = dao.update(user);

        Assertions.assertEquals(expected, result);
    }


}
