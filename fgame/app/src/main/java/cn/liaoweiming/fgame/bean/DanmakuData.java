package cn.liaoweiming.fgame.bean;

import java.util.ArrayList;
import java.util.List;

import cn.liaoweiming.fgame.danmaku.DefaultDanmakuFilter;

public class DanmakuData {
    Long last_tm;
    List<DanmakuItem> msg_list;

    public DanmakuData() {
        msg_list = new ArrayList<>();
    }

    public DanmakuData(Long last_tm, List<DanmakuItem> msg_list) {
        this.last_tm = last_tm;
        this.msg_list = msg_list;
    }

    public Long getLast_tm() {
        return last_tm;
    }

    public void setLast_tm(Long last_tm) {
        this.last_tm = last_tm;
    }

    public List<DanmakuItem> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(List<DanmakuItem> msg_list) {
        this.msg_list = msg_list;
    }

    @Override
    public String toString() {
        return "DanmakuData{" +
                "last_tm=" + last_tm +
                ", msg_list=" + msg_list +
                '}';
    }

    public String getDanmakuListString(){
        List<String> list = new ArrayList<>(msg_list.size());
        for (DanmakuItem danmakuItem : msg_list){
            if(DefaultDanmakuFilter.isValid(danmakuItem))
                list.add(danmakuItem.getContent());
        }
        return list.toString();
    }
}
