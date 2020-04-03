package com.brauma.withinyourmeans.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.brauma.withinyourmeans.Model.Balance;
import com.brauma.withinyourmeans.Model.Bar;
import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "wym.db";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_DATE = "date";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String KEY_COLOR = "color";
    private static final String KEY_ICON = "icon";

    private static final String TABLE_BALANCE = "balances";
    private static final String KEY_BUDGET = "budget";
    private static final String KEY_SPENT = "spent";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
                + KEY_COLOR + " TEXT," + KEY_ICON + " INTEGER)";

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_CATEGORY_ID + " INTEGER," + KEY_AMOUNT + " INTEGER," + KEY_DATE + " INTEGER,"
                + " FOREIGN KEY (" + KEY_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_ID+ "))";

        String CREATE_BALANCE_TABLE = "CREATE TABLE " + TABLE_BALANCE + "("
                + KEY_DATE + " INTEGER PRIMARY KEY, " + KEY_BUDGET + " INTEGER, " + KEY_SPENT + ")";

        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_BALANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }


    public boolean insertExpense(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, expense.get_name());
        contentValues.put(KEY_CATEGORY_ID, getCategoryIdByName(expense.get_category()));
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

    public boolean insertBalance(Balance balance){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, balance.getDate());
        contentValues.put(KEY_BUDGET, balance.getBudget());
        contentValues.put(KEY_SPENT, balance.getSpent());

        long result = db.insert(TABLE_BALANCE, null, contentValues);
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

    public void updateBalance(Balance balance){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_BUDGET, balance.getBudget()); //These Fields should be your String values of actual column names
        cv.put(KEY_SPENT, balance.getSpent());
        cv.put(KEY_DATE, balance.getDate());

        db.update(TABLE_BALANCE, cv, KEY_DATE + "=" + balance.getDate(), null);

    }

    private Integer getCategoryIdByName(String name){
        Integer id = null;
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_CATEGORIES + " WHERE " + KEY_NAME + " = \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        // return contact list
        return id;
    }

    public ArrayList<Category> getCategories(){
        ArrayList<Category> categoryList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " ORDER BY " + KEY_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.set_name(cursor.getString(1));
                category.set_color(cursor.getString(2));
                category.set_icon(cursor.getInt(3));

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

                // Adding expense to list
                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return expenses list
        return expensesList;
    }

    public ArrayList<Balance> getBalances(){
        ArrayList<Balance> balancesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BALANCE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Balance balance = new Balance();
                balance.setDate(cursor.getLong(0));
                balance.setBudget(Integer.parseInt(cursor.getString(1)));
                balance.setSpent(Integer.parseInt(cursor.getString(2)));

                // Adding balance to list
                balancesList.add(balance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return Balance list
        return balancesList;
    }

    public Balance getBalanceByDate(long date){
        Balance balance = null;
        String selectQuery = "SELECT  * FROM " + TABLE_BALANCE + " WHERE " + KEY_DATE + " = " + date;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                balance = new Balance();
                balance.setDate(cursor.getLong(0));
                balance.setBudget(Integer.parseInt(cursor.getString(1)));
                balance.setSpent(Integer.parseInt(cursor.getString(2)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return Balance list
        return balance;
    }

    public ArrayList<Expense> getExpensesBetweenDates(long from, long to){
        ArrayList<Expense> expensesList = new ArrayList<Expense>();
        // Select All Query
        String selectQuery = "SELECT " + TABLE_EXPENSES + "." + KEY_ID + ", " + TABLE_EXPENSES + "." + KEY_NAME + ", "
                + TABLE_CATEGORIES + "." + KEY_NAME + ", "  + KEY_AMOUNT + ", " + KEY_DATE
                + " FROM " + TABLE_EXPENSES + ", " + TABLE_CATEGORIES
                + " WHERE " + KEY_DATE + " BETWEEN " + from + " AND " + to
                + " AND " + TABLE_CATEGORIES + "." + KEY_ID + "= " + KEY_CATEGORY_ID
                + " ORDER BY " + KEY_DATE + " DESC";

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
        Log.e("DEBUG", String.valueOf(expensesList.size()));
        // return contact list
        return expensesList;
    }

    public ArrayList<Bar> getCategorySumsBetweenDates(long from, long to){
        ArrayList<Bar> sums = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT SUM(" + KEY_AMOUNT + "), " + TABLE_CATEGORIES + "." + KEY_NAME + ", " + KEY_COLOR + " FROM " + TABLE_EXPENSES + ", " + TABLE_CATEGORIES
                + " WHERE " + KEY_DATE + " BETWEEN " + from
                + " AND " + to + " AND " + TABLE_EXPENSES + "." + KEY_CATEGORY_ID + " = " + TABLE_CATEGORIES + "." + KEY_ID
                + " GROUP BY " + TABLE_CATEGORIES + "." + KEY_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bar sum = new Bar();
                sum.setValue(Integer.parseInt(cursor.getString(0)));
                sum.setName(cursor.getString(1));
                sum.setColor(Integer.parseInt(cursor.getString(2)));

                sums.add(sum);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return sums;
    }

}
