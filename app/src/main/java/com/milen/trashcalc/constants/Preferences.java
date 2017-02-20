package com.milen.trashcalc.constants;


import java.text.DecimalFormat;

public class Preferences {
    public static final String FB_ENTITIES_TABLE = "entities";
    public static final String FB_CHILD_DATE = "date";

    public static final String DECIMAL_FORMAT_STRING = "#.##";
    public static final String NODE_DATE_FORMAT_STRING = "yyyy-MM-dd_hh:mm:ss";
    public static final String EMAIL_DATE_FORMAT_STRING = "yyyy-MM-dd hh:mm:ss";

    public static final String DATE_PREFIX = "дата: ";
    public static final String TRUCK_ID_PREFIX = "карта номер: ";
    public static final String WEIGHT_SUFFIX = ", кг.";
    public static final String WEIGHT_TOTAL = "Общо: ";
    public static final String EMAIL_SUBJECT = "Доклад от разделно събиране на отпадъци, за последния месец.";
    public static final String NO_ENTITIES = "Няма направени доставки през отчения период!";
}
