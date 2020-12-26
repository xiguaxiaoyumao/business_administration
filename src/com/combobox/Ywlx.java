package com.combobox;

public class Ywlx {
    public String getYwlxid() {
        return ywlxid;
    }

    public void setYwlxid(String ywlxid) {
        this.ywlxid = ywlxid;
    }

    public String getYwlxname() {
        return ywlxname;
    }

    public void setYwlxname(String ywlxname) {
        this.ywlxname = ywlxname;
    }

    private String ywlxid;
    private String ywlxname;

    @Override
    public String toString() {
        return ywlxname;
    }
}
