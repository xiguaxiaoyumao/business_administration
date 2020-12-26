package com.combobox;

public class Qyzl {
    private String qyzlid;
    private String qyzlname;

    public String getQyzlid() {
        return qyzlid;
    }

    public void setQyzlid(String qyzlid) {
        this.qyzlid = qyzlid;
    }

    public String getQyzlname() {
        return qyzlname;
    }

    public void setQyzlname(String qyzlname) {
        this.qyzlname = qyzlname;
    }

    @Override
    public String toString() {
        return qyzlname;
    }
}
