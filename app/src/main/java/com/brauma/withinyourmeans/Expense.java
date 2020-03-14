package com.brauma.withinyourmeans;

public class Expense {
    int _id;
    int _amount;
    String _name;
    String _category;
    String _description;

    public Expense(){}

    public Expense(int amount, String name, String category, String description){
        this._amount = amount;
        this._name = name;
        this._category = category;
        this._description = description;
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

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
}
