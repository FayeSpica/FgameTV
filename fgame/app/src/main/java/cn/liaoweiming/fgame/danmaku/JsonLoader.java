package cn.liaoweiming.fgame.danmaku;

import java.io.InputStream;
import java.util.List;

import cn.liaoweiming.fgame.bean.DanmakuItem;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.parser.IDataSource;

public class JsonLoader implements ILoader {
    private static volatile JsonLoader instance;
    private JsonSource source;
    @Override
    public JsonSource getDataSource() {
        return source;
    }

    public static JsonLoader instance() {
        if(instance == null){
            synchronized (JsonLoader.class){
                if(instance == null)
                    instance = new JsonLoader();
            }
        }
        return instance;
    }

    @Override
    public void load(String uri) throws IllegalDataException {

    }

    @Override
    public void load(InputStream in) throws IllegalDataException {

    }

    public void load(List<DanmakuItem> list) throws IllegalDataException {
        source = new JsonSource(list);
    }
}
