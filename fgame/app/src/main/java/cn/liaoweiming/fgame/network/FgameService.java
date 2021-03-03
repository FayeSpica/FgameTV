package cn.liaoweiming.fgame.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.liaoweiming.fgame.bean.DanmakuData;
import cn.liaoweiming.fgame.bean.LiveInfo;
import cn.liaoweiming.fgame.bean.LiveLayout;
import cn.liaoweiming.fgame.bean.Stream;
import cn.liaoweiming.fgame.bean.fgame.Fresponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FgameService {
    static final String TAG = "FgameService";

    Retrofit retrofit;
    LiveReqInterface liveReqInterface;

    private FgameService(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://share.egame.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        liveReqInterface = retrofit.create(LiveReqInterface.class);
    }

    public static FgameService getInstance(){
        return SingletonInstance.instance;
    }

    private static class SingletonInstance{
        static FgameService instance = new FgameService();
    }

    public List<Stream> getStreamListByAnchorId(Long anchorId){
        List<Stream> list;
        try{
            Call<ResponseBody> call = liveReqInterface.getPid(anchorId);
            Response<ResponseBody> response = call.execute();
            Pattern pattern = Pattern.compile("streamInfos:\\[(.+?)\\]");
            Matcher matcher = pattern.matcher(response.body().string());

            while (matcher.find()){
                String listStr = "["+matcher.group(1)+"]";
                System.out.println(listStr);
                Gson gson = new Gson();
                list = gson.fromJson(listStr, new TypeToken<List<Stream>>(){}.getType());
                //Log.d(TAG,"getStreamByAnchorId("+anchorId+")= "+list);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        list = new ArrayList<>();
        return list;
    }

    public String getPidByAnchorId(Long anchorId){
        try{
            Call<ResponseBody> call = liveReqInterface.getPid(anchorId);
            Response<ResponseBody> response = call.execute();
            Pattern pattern = Pattern.compile("pid\":\"(.+?)\"\\}");
            Matcher matcher = pattern.matcher(response.body().string());

            if (matcher.find()){
                //System.out.println(matcher.group(1));
                Log.d(TAG,"getPidByAnchorId("+anchorId+") pid="+matcher.group(1));
                return matcher.group(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
        //Log.d(TAG,"getPidByAnchorId("+anchorId+")= "+pid);
    }

    public DanmakuData getDanmakuData(Long anchor_id, String vid, Long last_tm){
        Fresponse fresponse;
        try{
            String param = "{\"key\":{\"module\":\"pgg_live_barrage_svr\",\"method\":\"get_barrage\",\"param\":{\"anchor_id\":"+anchor_id+",\"vid\":\""+vid+"\",\"last_tm\":"+last_tm+"}}}";
            System.out.println(param);
            String appInfo = "{\"platform\":4,\"terminal_type\":2,\"egame_id\":\"egame_official\",\"version_code\":\"9.9.9.9\",\"version_name\":\"9.9.9.9\"}";
            Call<Fresponse> call = liveReqInterface.getDanmakuList(param,appInfo,"1");
            Response<Fresponse> response = call.execute();
            //System.out.println(response.body());
            fresponse = response.body();
        }catch (Exception e){
            e.printStackTrace();
            fresponse = new Fresponse();
        }
        return fresponse.getDanmakuData();
    }

    public List<LiveInfo> getLiveListByAppId(String appId, int pageNum, int pageSize){
        Response<Fresponse> response;
        try{
            String param = "{\"key\":{\"module\":\"pgg_live_read_ifc_mt_svr\",\"method\":\"get_pc_live_list\",\"param\":{\"appid\":\""+appId+"\",\"page_num\":"+pageNum+",\"page_size\":"+pageSize+",\"tag_id\":0,\"tag_id_str\":\"\"}}}";
            String appInfo = "{\"platform\":4,\"terminal_type\":2,\"egame_id\":\"egame_official\",\"version_code\":\"9.9.9.9\",\"version_name\":\"9.9.9.9\",\"ext_info\":{\"_qedj_t\":\"\",\"ALG-flag_type\":\"\",\"ALG-flag_pos\":\"\"}";
            Call<Fresponse> call = liveReqInterface.getLiveList(param,appInfo,"1");
            response = call.execute();
            //System.out.println(response.body());
            return response.body().getLiveInfoList();
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<LiveLayout> getLayoutList(){
        List<LiveLayout> liveLayoutList = new ArrayList<>(100);
        try{
            Call<ResponseBody> call = liveReqInterface.getLayoutList();
            Response<ResponseBody> response = call.execute();
            Pattern pattern = Pattern.compile("/livelist\\?layoutid=(.+?)\".+?title=\"(.+?)\".+?>");//"/livelist\?layoutid=(.+?)\".+?title=\"(.+?)\".+?>");
            Matcher matcher = pattern.matcher(response.body().string());

            while (matcher.find()){
                liveLayoutList.add(new LiveLayout(matcher.group(1),matcher.group(2)));
            }
            Log.d(TAG, "getLayoutList = " + liveLayoutList);
            //System.out.println(liveLayoutList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return liveLayoutList;
    }


}
