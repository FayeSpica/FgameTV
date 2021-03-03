package cn.liaoweiming.fgame.bean.fgame;

import java.util.ArrayList;
import java.util.List;

import cn.liaoweiming.fgame.bean.DanmakuData;
import cn.liaoweiming.fgame.bean.LiveInfo;

public class Fresponse {
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Fresponse{" +
                "data=" + data +
                '}';
    }

    private boolean isDataInnerValid(){
        return data!=null&&data.key!=null&&data.key.retBody!=null&&data.key.retBody.data!=null;
    }

    public List<LiveInfo> getLiveInfoList(){
        if(isDataInnerValid()&&data.key.retBody.data.live_data!=null){
            return data.key.retBody.data.live_data.live_list;
        }else {
            return new ArrayList<>();
        }
    }

    public DanmakuData getDanmakuData(){
        DanmakuData danmakuData = new DanmakuData();
        if(isDataInnerValid()){
            danmakuData.setLast_tm(data.key.retBody.data.last_tm);
            danmakuData.setMsg_list(data.key.retBody.data.msg_list);
        }
        return danmakuData;
    }
}
