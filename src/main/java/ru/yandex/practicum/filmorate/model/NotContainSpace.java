package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotContainSpaceValidator.class)
@Documented
public @interface NotContainSpace {
    String message() default "значение не должно содержать пробелы";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}