package com.example.amir.mydiabetes;

/**
 * Created by or on 08/01/2018.
 */

import android.provider.BaseColumns;

public final class Constants {
    private Constants(){
        throw new AssertionError("Can't create constants class");
    }
    public static abstract class diabetesTable implements BaseColumns {
        public static final String TABLE_NAME = "MyDiabetes";
        public static final String GLUCOSE = "glucose";
        public static final String CARBO = "Carbo";
        public static final String INSULIN = "insulin";
        public static final String DATE = "date";

    }
}
