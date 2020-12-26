package com.combobox;

public class Gj {
    private String gjid;
    private String gjname;

    public String getGjid() {
        return gjid;
    }

    public void setGjid(String gjid) {
        this.gjid = gjid;
    }

    public String getGjname() {
        return gjname;
    }

    public void setGjname(String gjname) {
        this.gjname = gjname;
    }

    @Override
    public String toString() {
        return gjname;
    }
}
