package ru.sergeyshokhin.controller.validator;

public class UserValidator {
    public boolean validate(String value, String key) {
        if (value == null || key==null) return false;
        if (value.isEmpty()) return false;
        switch (key) {
            case "age":
                return checkAge(value);

            case "name":
                return checkName(value);

            case "email":
                return checkEmail(value);

            case "id":
                return checkId(value);

            default:
                return false;
        }
    }

    private boolean checkAge(String value) {
        return isInt(value) && value.matches("^[1-9]\\d?$");
    }

    private boolean checkId(String value) {
        if (!isInt(value)) return false;
        if (!value.matches("^[1-9]\\d*$")) return false;
        int id = Integer.parseInt(value);
        return id > 0  ;
    }

    private boolean isInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean checkName(String value) {
        return value.length() < 26 && value.matches("^[A-Z][a-z]+$");
    }

    private boolean checkEmail(String value) {
        return value.length() < 51 && value.matches("^[\\w.%+-]+@[a-z0-9-]+\\.[a-z]{2,3}$");
    }
}
