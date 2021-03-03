package cn.liaoweiming.fgame.bean.fgame;

import java.util.List;

import cn.liaoweiming.fgame.bean.DanmakuItem;

public class DataInner {
    Integer total;
    LiveData live_data;
    Integer is_get_over;

    Long last_tm;
    List<DanmakuItem> msg_list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public LiveData getLive_data() {
        return live_data;
    }

    public void setLive_data(LiveData live_data) {
        this.live_data = live_data;
    }

    public Integer getIs_get_over() {
        return is_get_over;
    }

    public void setIs_get_over(Integer is_get_over) {
        this.is_get_over = is_get_over;
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
        return "DataInner{" +
                "total=" + total +
                ", live_data=" + live_data +
                ", is_get_over=" + is_get_over +
                ", last_tm=" + last_tm +
                ", msg_list=" + msg_list +
                '}';
    }
}
