package com.milen.trashcalc.utils;

import android.net.ParseException;

public class Validator {
    public static final int MIN_TRUCK_WEIGHT = 1000;
    public static final int CODE_LENGTH = 13;//CODE_LENGTH must be 13

    public static boolean isValidCode(String code) {
        return code != null && code.length() == CODE_LENGTH;
    }

    public static boolean isValidateDiff(double fieldFull, double fieldEmpty) {
        return !(fieldFull < MIN_TRUCK_WEIGHT ||
                fieldEmpty < MIN_TRUCK_WEIGHT ||
                fieldFull <= fieldEmpty);
    }
}
