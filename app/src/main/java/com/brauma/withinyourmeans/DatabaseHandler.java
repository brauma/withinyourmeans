package com.brauma.withinyourmeans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wym.db";
    private static final String TABLE_NAME= "expenses";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_AMOUNT + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, expense.get_name());
        contentValues.put(KEY_DESC, expense.get_description());
        contentValues.put(KEY_CATEGORY, expense.get_category());
        contentValues.put(KEY_AMOUNT, expense.get_amount());
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if(result != -1){
            return true;
        }
        return false;
    }

    public void deleteData(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(expense.get_id()) });
        db.close();
    }

    public List<Expense> getExpenses(){
        List<Expense> expensesList = new ArrayList<Expense>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.set_id(Integer.parseInt(cursor.getString(0)));
                expense.set_name(cursor.getString(1));
                expense.set_description(cursor.getString(2));
                expense.set_category(cursor.getString(3));
                expense.set_amount(cursor.getInt(4));
                // Adding contact to list
                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return expensesList;
    }
}
