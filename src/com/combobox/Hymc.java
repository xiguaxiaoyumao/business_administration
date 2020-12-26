package com.combobox;

public class Hymc {
    public String getHymcid() {
        return hymcid;
    }

    public void setHymcid(String hymcid) {
        this.hymcid = hymcid;
    }

    public String getHymcname() {
        return hymcname;
    }

    public void setHymcname(String hymcname) {
        this.hymcname = hymcname;
    }

    private String hymcid;
    private String hymcname;


    @Override
    public String toString() {
        return hymcname;
    }
}
