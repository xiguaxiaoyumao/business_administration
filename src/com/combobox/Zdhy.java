package com.combobox;

public class Zdhy {
    public String getZdhyname() {
        return zdhyname;
    }

    public void setZdhyname(String zdhyname) {
        this.zdhyname = zdhyname;
    }

    public String getZdhyid() {
        return zdhyid;
    }

    public void setZdhyid(String zdhyid) {
        this.zdhyid = zdhyid;
    }

    private String zdhyname;
    private String zdhyid;

    @Override
    public String toString() {
        return zdhyname;
    }
}
