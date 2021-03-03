package cn.liaoweiming.fgame.bean.fgame;

import java.util.List;

import cn.liaoweiming.fgame.bean.LiveInfo;

public class LiveData {
    List<LiveInfo> live_list;

    public List<LiveInfo> getLive_list() {
        return live_list;
    }

    public void setLive_list(List<LiveInfo> live_list) {
        this.live_list = live_list;
    }

    @Override
    public String toString() {
        return "LiveData{" +
                "live_list=" + live_list +
                '}';
    }
}
