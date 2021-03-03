package cn.liaoweiming.fgame.danmaku;

import java.util.List;

import cn.liaoweiming.fgame.bean.DanmakuItem;
import master.flame.danmaku.danmaku.parser.IDataSource;

public class JsonSource implements IDataSource<List<DanmakuItem>> {
    List<DanmakuItem> list;
    public  JsonSource(List<DanmakuItem> list){
        this.list = list;
    }
    @Override
    public List<DanmakuItem> data() {
        return list;
    }

    @Override
    public void release() {
        list = null;
    }
}
