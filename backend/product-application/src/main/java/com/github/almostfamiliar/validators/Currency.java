package com.github.almostfamiliar.validators;

import com.github.almostfamiliar.port.out.ValidateCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = Currency.CurrencyValidator.class)
public @interface Currency {

  String message() default
      "Unsupported currency! Every currency must also have at least 3 characters!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Component
  @RequiredArgsConstructor
  class CurrencyValidator implements ConstraintValidator<Currency, String> {
    private final ValidateCurrency validateCurrency;

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
      if (currency != null) {
        if (currency.length() != 3) {
          return false;
        }
        final Set<String> validCurrencies = validateCurrency.getValidCurrencies();

        return validCurrencies.contains(currency.toUpperCase());
      }
      return true;
    }
  }
}
