package com.example.amir.mydiabetes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by or on 08/01/2018.
 */

public class AssignmentsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Constants.diabetesTable.TABLE_NAME + " (" +
                    Constants.diabetesTable._ID +           " INTEGER PRIMARY KEY," +
                    Constants.diabetesTable.GLUCOSE +     " FLOAT NOT NULL," +
                    Constants.diabetesTable.CARBO +    " FLOAT NOT NULL," +
                    Constants.diabetesTable.INSULIN +    " INTEGER NOT NULL,"+
                    Constants.diabetesTable.DATE +    " TEXT NOT NULL"+
                    ");";
    private static AssignmentsDbHelper instance;

    public static synchronized AssignmentsDbHelper getHelper(Context context) {
        if (instance == null)
            instance = new AssignmentsDbHelper(context);
        return instance;
    }
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Constants.diabetesTable.TABLE_NAME;
    public static final String DATABASE_NAME = "myDiabetes.db";

    public AssignmentsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // implement migration policy if have
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
