package cn.liaoweiming.fgame.bean;

import java.io.Serializable;

public class VideoInfo implements Serializable {
    String vid;
    String url;
    String dist;
    String play_url;
    String url_high_reslution;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getUrl_high_reslution() {
        return url_high_reslution;
    }

    public void setUrl_high_reslution(String url_high_reslution) {
        this.url_high_reslution = url_high_reslution;
    }
}
