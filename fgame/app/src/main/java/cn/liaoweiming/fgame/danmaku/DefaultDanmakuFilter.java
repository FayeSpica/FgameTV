package cn.liaoweiming.fgame.danmaku;

import android.graphics.Color;
import android.util.Log;

import cn.liaoweiming.fgame.bean.DanmakuItem;

public class DefaultDanmakuFilter {
    private static String TAG = "DefaultDanmakuFilter";
    private DefaultDanmakuFilter(){}

    public static boolean isValid(DanmakuItem danmakuItem){
        //danmakuItem.setContent(danmakuItem.getNick()+":"+danmakuItem.getContent());

        if(danmakuItem.getExt().getLv()!=null&&Integer.parseInt(danmakuItem.getExt().getLv())>=10){
            danmakuItem.setContent(danmakuItem.getNick()+":"+danmakuItem.getContent());
        }
        switch (danmakuItem.getType()){
//            case 0:  // 普通
//            case 9:  // 普通？
//            case 35: // 贵族进入房间通知
//            case 36: // 开通贵族
            case 7:
            case 24:
            case 28:
            case 29:
            case 33:
            case 35:
                Log.d(TAG,"DefaultDanmakuFilter isValid false"+danmakuItem);
                return false;
            default:
                Log.d(TAG,"DefaultDanmakuFilter isValid true"+danmakuItem);
                return true;
        }
    }

    public static int getColor(DanmakuItem danmakuItem){
        int lv = danmakuItem.getExt().getLv() == null ? 0 : Integer.parseInt(danmakuItem.getExt().getLv());
        if(lv<10){
            return Color.WHITE;
        }else {
            return Color.alpha(0x33CCFF);
        }
    }
    public static int getShadowColor(DanmakuItem danmakuItem){
        int lv = danmakuItem.getExt().getLv() == null ? 0 : Integer.parseInt(danmakuItem.getExt().getLv());
        if(lv<10){
            return Color.BLACK;
        }else {
            return Color.WHITE;
        }
    }
}
