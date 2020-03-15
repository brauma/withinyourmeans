package com.brauma.withinyourmeans.Model;

public class Expense {
    int _id;
    int _amount;
    String _name;
    String _category;
    long _date;

    public Expense(){}

    public Expense(int amount, String name, String category, long date){
        this._amount = amount;
        this._name = name;
        this._category = category;
        this._date = date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_amount() {
        return _amount;
    }

    public void set_amount(int _amount) {
        this._amount = _amount;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public long get_date() {
        return _date;
    }

    public void set_date(long date) {
        this._date = date;
    }
}
