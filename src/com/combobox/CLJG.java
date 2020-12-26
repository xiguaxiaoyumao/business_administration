package com.combobox;

public class CLJG {
    public String getCljgid() {
        return cljgid;
    }

    public void setCljgid(String cljgid) {
        this.cljgid = cljgid;
    }

    public String getCljgname() {
        return cljgname;
    }

    public void setCljgname(String cljgname) {
        this.cljgname = cljgname;
    }

    String cljgid;
    String cljgname;

    @Override
    public String toString() {
        return cljgname;
    }
}
