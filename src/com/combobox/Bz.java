package com.combobox;

public class Bz {
    private String bzid;
    private String bzname;

    public String getBzid() {
        return bzid;
    }

    public void setBzid(String bzid) {
        this.bzid = bzid;
    }

    public String getBzname() {
        return bzname;
    }

    public void setBzname(String bzname) {
        this.bzname = bzname;
    }

    @Override
    public String toString() {
        return bzname;
    }
}
