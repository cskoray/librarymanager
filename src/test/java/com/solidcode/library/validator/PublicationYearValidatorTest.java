package com.solidcode.library.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicationYearValidatorTest {

    private PublicationYearValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PublicationYearValidator();
    }

    @Test
    void testValidPublicationYear() {
        assertTrue(validator.isValid(2023, null));
        assertTrue(validator.isValid(1995, null));
    }

    @Test
    void testInvalidPublicationYear() {
        int futureYear = 2050;
        assertFalse(validator.isValid(futureYear, null));
    }
}
