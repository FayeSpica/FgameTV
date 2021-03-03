package cn.liaoweiming.fgame.bean;

public class Stream {
    String playUrl;
    String desc;

    @Override
    public String toString() {
        return "Stream{" +
                "playUrl='" + playUrl + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
