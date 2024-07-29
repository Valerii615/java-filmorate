package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotContainSpaceValidator implements ConstraintValidator<NotContainSpace, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null && !value.isEmpty()) {
            return !value.contains(" ");
        }
        return true;
    }
}
