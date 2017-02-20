package com.milen.trashcalc;

import com.milen.trashcalc.utils.Validator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TrashCalcValidatorUnitTest {

    @Test
    public void validateEnteredCode() throws Exception {
        assertEquals(Validator.isValidCode("1234567890123"), true);
        assertEquals(Validator.isValidCode("123456789012"), false);
        assertEquals(Validator.isValidCode("12345678901234"), false);
        assertEquals(Validator.isValidCode(null), false);
    }

    @Test
    public void validateIfEnteredValuesWillProduceValidDifference() throws Exception {
        assertEquals(Validator.isValidateDiff(
                    Validator.MIN_TRUCK_WEIGHT + 500, Validator.MIN_TRUCK_WEIGHT ), true
        );
        assertEquals(Validator.isValidateDiff(
                Validator.MIN_TRUCK_WEIGHT + 556.06, Validator.MIN_TRUCK_WEIGHT + 56.06), true
        );
        assertEquals(Validator.isValidateDiff(
                Validator.MIN_TRUCK_WEIGHT + 22556.86, Validator.MIN_TRUCK_WEIGHT + 1256.06), true
        );
        assertEquals(Validator.isValidateDiff(
                Validator.MIN_TRUCK_WEIGHT, Validator.MIN_TRUCK_WEIGHT), false //must not be equal
        );

        int valueUnderMinimum = Validator.MIN_TRUCK_WEIGHT - 1;
        int valueMoreThenMinimum = Validator.MIN_TRUCK_WEIGHT + 1;
        assertEquals(Validator.isValidateDiff(valueUnderMinimum, valueUnderMinimum),false);
        assertEquals(Validator.isValidateDiff(valueUnderMinimum, Validator.MIN_TRUCK_WEIGHT), false);
        assertEquals(Validator.isValidateDiff(valueMoreThenMinimum, valueUnderMinimum), false);
        assertEquals(Validator.isValidateDiff(valueUnderMinimum, valueMoreThenMinimum), false);
    }

}