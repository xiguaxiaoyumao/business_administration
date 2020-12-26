package com.combobox;

public class Qylx {
    private String qylxid;
    private String qylxname;

    public String getQylxid() {
        return qylxid;
    }

    public void setQylxid(String qylxid) {
        this.qylxid = qylxid;
    }

    public String getQylxname() {
        return qylxname;
    }

    public void setQylxname(String qylxname) {
        this.qylxname = qylxname;
    }

    @Override
    public String toString() {
        return qylxname;
    }
}
