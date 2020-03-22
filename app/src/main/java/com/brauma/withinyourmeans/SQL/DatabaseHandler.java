package com.brauma.withinyourmeans.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "wym.db";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String KEY_COLOR = "color";
    private static final String KEY_ICON = "icon";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_NAME + " TEXT PRIMARY KEY,"
                + KEY_COLOR + " TEXT," + KEY_ICON + " INTEGER)";

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_CATEGORY + " INTEGER," + KEY_AMOUNT + " INTEGER," + KEY_DATE + " INTEGER,"
                + " FOREIGN KEY (" + KEY_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_NAME + "))";

        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public boolean insertExpense(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, expense.get_name());
        contentValues.put(KEY_CATEGORY, expense.get_category());
        contentValues.put(KEY_AMOUNT, expense.get_amount());
        contentValues.put(KEY_DATE, expense.get_date());

        long result = db.insert(TABLE_EXPENSES, null, contentValues);
        db.close();

        if(result != -1){
            return true;
        }
        return false;
    }

    public boolean insertCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, category.get_name());
        contentValues.put(KEY_COLOR, category.get_color());
        contentValues.put(KEY_ICON, category.get_icon());

        long result = db.insert(TABLE_CATEGORIES, null, contentValues);
        db.close();

        if(result != -1){
            return true;
        }

        return false;
    }

    public void deleteExpense(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[] { String.valueOf(expense.get_id()) });
        db.close();
    }

    public ArrayList<Category> getCategories(){
        ArrayList<Category> categoryList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.set_name(cursor.getString(0));
                category.set_color(cursor.getString(1));
                category.set_icon(cursor.getInt(2));

                // Adding contact to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return categoryList;

    }

    public ArrayList<Expense> getExpenses(){
        ArrayList<Expense> expensesList = new ArrayList<Expense>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.set_id(Integer.parseInt(cursor.getString(0)));
                expense.set_name(cursor.getString(1));
                expense.set_category(cursor.getString(2));
                expense.set_amount(cursor.getInt(3));
                expense.set_date(cursor.getLong(4));

                // Adding contact to list
                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return expensesList;
    }

    //TODO ezt valahogy megcsinálni
    public int getSumByDate(int year, int month){

        String selectQuery = "SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_EXPENSES + " WHERE ";
        return 0;
    }
}
