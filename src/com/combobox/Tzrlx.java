package com.combobox;

public class Tzrlx {
    private String tzrid;
    private String tzrname;

    public String getTzrid() {
        return tzrid;
    }

    public void setTzrid(String tzrid) {
        this.tzrid = tzrid;
    }

    public String getTzrname() {
        return tzrname;
    }

    public void setTzrname(String tzrname) {
        this.tzrname = tzrname;
    }

    @Override
    public String toString() {
        return tzrname;
    }
}
