package cn.liaoweiming.fgame.bean;

public class Ext {
    String lv;
    String lvnew;
    String zn;

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getLvnew() {
        return lvnew;
    }

    public void setLvnew(String lvnew) {
        this.lvnew = lvnew;
    }

    public String getZn() {
        return zn;
    }

    public void setZn(String zn) {
        this.zn = zn;
    }

    @Override
    public String toString() {
        return "Ext{" +
                "lv='" + lv + '\'' +
                ", lvnew='" + lvnew + '\'' +
                ", zn='" + zn + '\'' +
                '}';
    }
}
