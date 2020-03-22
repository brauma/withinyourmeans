package com.brauma.withinyourmeans.Model;

public class Category {
    int _icon;
    String _name;
    String _color;

    public Category() {
    }

    public Category(int _icon, String _name, String _color) {
        this._icon = _icon;
        this._name = _name;
        this._color = _color;
    }

    public int get_icon() {
        return _icon;
    }

    public void set_icon(int _icon) {
        this._icon = _icon;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_color() {
        return _color;
    }

    public void set_color(String _color) {
        this._color = _color;
    }
}
