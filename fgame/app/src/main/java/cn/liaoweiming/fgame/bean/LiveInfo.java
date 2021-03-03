package cn.liaoweiming.fgame.bean;

import java.io.Serializable;

public class LiveInfo implements Serializable {
    VideoInfo video_info;
    String title;
    Long anchor_id;
    String anchor_name;
    Long online;
    String appid;
    String appname;
    String anchor_face_url;
    String city;
    Long fans_count;

    public VideoInfo getVideo_info() {
        return video_info;
    }

    public void setVideo_info(VideoInfo video_info) {
        this.video_info = video_info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(Long anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getAnchor_name() {
        return anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAnchor_face_url() {
        return anchor_face_url;
    }

    public void setAnchor_face_url(String anchor_face_url) {
        this.anchor_face_url = anchor_face_url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getFans_count() {
        return fans_count;
    }

    public void setFans_count(Long fans_count) {
        this.fans_count = fans_count;
    }

    @Override
    public String toString() {
        return "LiveInfo{" +
                "video_info=" + video_info +
                ", title='" + title + '\'' +
                ", anchor_id=" + anchor_id +
                ", anchor_name='" + anchor_name + '\'' +
                ", online=" + online +
                ", appid='" + appid + '\'' +
                ", appname='" + appname + '\'' +
                ", anchor_face_url='" + anchor_face_url + '\'' +
                ", city='" + city + '\'' +
                ", fans_count=" + fans_count +
                '}';
    }
}
