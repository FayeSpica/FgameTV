package cn.liaoweiming.fgame.danmaku;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.liaoweiming.fgame.bean.DanmakuItem;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

public class BiliDanmakuJsonParser extends BaseDanmakuParser {
    List<DanmakuItem> danmakuItemList;
    public static String TAG = "BiliDanmakuJsonParser";
    public BiliDanmakuJsonParser() {
        super();
    }

    public BiliDanmakuJsonParser(List<DanmakuItem> danmakuItemList) {
        super();
        this.danmakuItemList = danmakuItemList;
    }


    @Override
    protected IDanmakus parse() {

        Log.d(TAG,"parse");

        Danmakus danmakus = new Danmakus();

        if(danmakuItemList==null){
            return danmakus;
        }

        Log.d(TAG,"parse danmakuItemList="+danmakuItemList);
        int i = 1;
        for (DanmakuItem danmakuItem : danmakuItemList){
            int type  = 1; // danmakuItem.getType();

            int color = 0x16777215;
            int textSize = 25;
            BaseDanmaku item = mContext.mDanmakuFactory.createDanmaku(type, mContext);
            if (item != null){
                item.flags = mContext.mGlobalFlagValues;
                //item.setTime(danmakuItem.getTm());
                item.text = danmakuItem.getContent();
                item.textSize = textSize * (mDispDensity - 0.6f);
                item.textColor = color;
                item.setTime(i*1000);
                item.setTimer(mTimer);
                danmakus.addItem(item);
            }
            i++;
        }

        return danmakus;
    }
}
