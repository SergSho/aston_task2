package ru.sergeyshokhin.controller.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class UserValidatorTest {
    private UserValidator validator = new  UserValidator();

    @DisplayName("Корректные данные.")
    @ParameterizedTest
    @CsvSource({"Ivan, name", "Example@mail.ru, email", "27, age", "123, id"})
    public void correctDataCheck(String value, String key) {
        Assertions.assertTrue(validator.validate(value, key));
    }

    @DisplayName("Некорректное значение: \"Null\" OR \"\".")
    @ParameterizedTest
    @NullAndEmptySource
    public void incorrectData_Value(String value) {
        Assertions.assertFalse(validator.validate(value, "name"));
    }

    @DisplayName("Некорректный ключ: \"Null\" OR \"\".")
    @ParameterizedTest
    @NullAndEmptySource
    public void incorrectData_Key(String key) {
        Assertions.assertFalse(validator.validate("Asdf", key));
    }

    @DisplayName("Некорректное значение параметра \"age\".")
    @ParameterizedTest
    @ValueSource(strings = {"027", "0", "100", "-1", "101"})
    public void incorrectData_Age(String age) {
        Assertions.assertFalse(validator.validate(age, "age"));
    }

    @DisplayName("Некорректное значение параметра \"name\".")
    @ParameterizedTest
    @ValueSource(strings = { "I", "IV","IVan", "iV", "ivan", "Ivan1"})
    public void incorrectData_Name(String name) {
        Assertions.assertFalse(validator.validate(name, "name"));
    }

    @DisplayName("Некорректное значение параметра \"email\".")
    @ParameterizedTest
    @ValueSource(strings = { "@mail.ru", "example.mail.ru","example@mail.ruru"})
    public void incorrectData_Email(String email) {
        Assertions.assertFalse(validator.validate(email, "email"));
    }

    @DisplayName("Некорректное значение параметра \"id\".")
    @ParameterizedTest
    @ValueSource(strings = { "0", "-7", "0250"})
    public void incorrectData_Id(String id) {
        Assertions.assertFalse(validator.validate(id, "id"));
    }
}
