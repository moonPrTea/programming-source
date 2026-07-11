package dev.moon.programming.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
  String message() default "Must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
