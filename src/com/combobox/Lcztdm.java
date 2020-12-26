package com.combobox;

public class Lcztdm {
    private String lcztdmid;
    private String lcztdmname;

    public String getLcztdmname() {
        return lcztdmname;
    }

    public void setLcztdmname(String lcztdmname) {
        this.lcztdmname = lcztdmname;
    }

    public String getLcztdmid() {
        return lcztdmid;
    }

    public void setLcztdmid(String lcztdmid) {
        this.lcztdmid = lcztdmid;
    }

    @Override
    public String toString() {
        return lcztdmname;
    }
}
