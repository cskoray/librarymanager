package com.solidcode.library.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class PublicationYearValidator implements ConstraintValidator<ValidPublicationYear, Integer> {

    @Override
    public void initialize(ValidPublicationYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value <= Year.now().getValue();
    }
}
