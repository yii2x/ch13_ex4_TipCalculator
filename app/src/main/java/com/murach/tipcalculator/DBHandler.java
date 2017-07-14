package com.murach.tipcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by 660161742 on 7/13/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // define database variables
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "tip_calculator.db";
    public static final String TABLE_TIPS = "tips";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BILL_DATE = "bill_date";
    public static final String COLUMN_BILL_AMOUNT = "bill_amount";
    public static final String COLUMN_TIP_PERCENT = "tip_percent";

    public static final Integer COLUMN_ID_COL = 0;
    public static final Integer COLUMN_BILL_DATE_COL = 1;
    public static final Integer COLUMN_BILL_AMOUNT_COL = 2;
    public static final Integer COLUMN_TIP_PERCENT_COL = 3;

    // define sqlite database variable
    private SQLiteDatabase database;

    // constructor
    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        database = db;
        // create database table
        String query = "CREATE TABLE " + TABLE_TIPS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BILL_DATE + " INTEGER NOT NULL, " +
                COLUMN_BILL_AMOUNT + " REAL NOT NULL, " +
                COLUMN_TIP_PERCENT + " REAL NOT NULL" + ");";
        db.execSQL(query);

        // add test records
        addTip(new Tip());
        addTip(new Tip(0, System.currentTimeMillis(), 40.60f, .15f));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // delete the entire table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        // recreate the table with the new properties
        onCreate(db);
    }

    public DBHandler open() throws SQLException {
        database = getWritableDatabase();   // get reference to the database
        return this;
    }

    // Add new row to the database
    public void addTip(Tip tip) {
        // content values is built into Android that allows you to add several values in one statement
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_DATE, tip.getDateMillis());
        values.put(COLUMN_BILL_AMOUNT, tip.getBillAmount());
        values.put(COLUMN_TIP_PERCENT, tip.getTipPercent());

       // SQLiteDatabase db =  getWritableDatabase();
        database.insert(TABLE_TIPS, null, values);

        // once your are done with database, then close it out to give memory back
        //close();
    }

    public ArrayList<Tip> getTips(){

        ArrayList<Tip> list = new ArrayList<Tip>();

        String[] allColumns = new String[] {
                COLUMN_ID, COLUMN_BILL_DATE,
                COLUMN_BILL_AMOUNT, COLUMN_TIP_PERCENT
        };

        Cursor c = database.query(TABLE_TIPS, allColumns, null, null, null,
                null, null);

        while(c.moveToNext()) {
            list.add(getTipFromCursor(c));
        }
        return list;
    }

    private static Tip getTipFromCursor(Cursor c){
        Tip tip = null;
        if(c != null && c.getCount() > 0){
            long id = c.getLong(COLUMN_ID_COL);
            long dateMillis = c.getLong(COLUMN_BILL_DATE_COL);
            float billAmount = c.getFloat(COLUMN_BILL_AMOUNT_COL);
            float tipPercent = c.getFloat(COLUMN_TIP_PERCENT_COL);

            tip = new Tip(
                    id,
                    dateMillis,
                    billAmount,
                    tipPercent);
        }
        return tip;
    }
}
