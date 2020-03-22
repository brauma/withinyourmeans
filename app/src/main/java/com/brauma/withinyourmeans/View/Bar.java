package com.brauma.withinyourmeans.View;

public class Bar implements Comparable<Bar>{
    private int color;
    private Integer value;

    public Bar(Integer value, int color) {
        this.color = color;
        this.value = value;
    }

    @Override
    public int compareTo(Bar o) {
        // descending
        return - this.getValue().compareTo(o.getValue());
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
