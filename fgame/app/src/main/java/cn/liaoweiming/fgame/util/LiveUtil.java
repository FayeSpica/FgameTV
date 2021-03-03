package cn.liaoweiming.fgame.util;

import java.util.ArrayList;
import java.util.List;

import cn.liaoweiming.fgame.bean.DanmakuItem;
import cn.liaoweiming.fgame.bean.LiveInfo;
import cn.liaoweiming.fgame.bean.Stream;

public class LiveUtil {
    private LiveUtil(){

    }

    public static String parseOnline(Long online){
        StringBuilder sb = new StringBuilder();
        sb.append(getOnline(online));
        return  sb.toString();
    }

    public static String parseOnline(LiveInfo liveInfo){
        StringBuilder sb = new StringBuilder();
        sb.append(getOnline(liveInfo.getOnline()));
        return  sb.toString();
    }

    private static String getOnline(Long online){
        String onlineStr = String.valueOf(online);
        int length = onlineStr.length();
        if(length<5){ // 人气小于一万不需要单位
            return onlineStr;
        }else {
            Long dot = (online - online/10000*10000)/1000;
            online = online/10000;
            onlineStr = online + "." + dot + "W";
            return onlineStr;
        }
    }

    public static List<String> getStreamStringList(List<Stream> streamList){
        List<String> list= new ArrayList<>(streamList.size());
        for(int i=0;i<streamList.size();i++){
            list.add(streamList.get(i).getDesc());
        }
        return list;
    }
}
