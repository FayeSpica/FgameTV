package cn.liaoweiming.fgame.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.liaoweiming.fgame.bean.LiveLayout;
import cn.liaoweiming.fgame.bean.Stream;
import cn.liaoweiming.fgame.bean.fgame.Fresponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://share.egame.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LiveReqInterface reqInterface = retrofit.create(LiveReqInterface.class);
        long anchorId = 77777;
        Call<ResponseBody> call = reqInterface.getPid(anchorId);
        try{
            Response<ResponseBody> response = call.execute();
            Pattern pattern = Pattern.compile("streamInfos:\\[(.+?)\\]");
            Matcher matcher = pattern.matcher(response.body().string());

            while (matcher.find()){
                String listStr = "["+matcher.group(1)+"]";
                System.out.println(listStr);
                Gson gson = new Gson();
                List<Stream> list = gson.fromJson(listStr, new TypeToken<List<Stream>>(){}.getType());
                //Log.d(TAG,"getStreamByAnchorId("+anchorId+")= "+list);
                System.out.println(list);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void mainPid(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://share.egame.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LiveReqInterface reqInterface = retrofit.create(LiveReqInterface.class);
        long anchorId = 77777;
        Call<ResponseBody> call = reqInterface.getPid(anchorId);
        try{
            Response<ResponseBody> response = call.execute();
            Pattern pattern = Pattern.compile("pid\":\"(.+?)\"\\}");
            Matcher matcher = pattern.matcher(response.body().string());

            if (matcher.find()){
                System.out.println(matcher.group(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void mainDanmaku(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://share.egame.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LiveReqInterface reqInterface = retrofit.create(LiveReqInterface.class);
        long anchor_id = 497383565;
        String vid = "497383565_1581940806";
        long last_tm = 1581957806;
        String param = "{\"key\":{\"module\":\"pgg_live_barrage_svr\",\"method\":\"get_barrage\",\"param\":{\"anchor_id\":"+anchor_id+",\"vid\":\""+vid+"\",\"last_tm\":"+last_tm+"}}}";
        System.out.println(param);
        String appInfo = "{\"platform\":4,\"terminal_type\":2,\"egame_id\":\"egame_official\",\"version_code\":\"9.9.9.9\",\"version_name\":\"9.9.9.9\"}";
        Call<Fresponse> call = reqInterface.getDanmakuList(param,appInfo,"1");
        try{
            Response<Fresponse> response = call.execute();
            System.out.println(response.body());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void mainLayoutList(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://share.egame.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LiveReqInterface reqInterface = retrofit.create(LiveReqInterface.class);
        Call<ResponseBody> call = reqInterface.getLayoutList();

        List<LiveLayout> liveLayoutList = new ArrayList<>(100);
        try{
            Response<ResponseBody> response = call.execute();
            Pattern pattern = Pattern.compile("/livelist\\?layoutid=(.+?)\".+?title=\"(.+?)\".+?>");//"/livelist\?layoutid=(.+?)\".+?title=\"(.+?)\".+?>");
            Matcher matcher = pattern.matcher(response.body().string());

            while (matcher.find()){
                liveLayoutList.add(new LiveLayout(matcher.group(1),matcher.group(2)));
            }
            System.out.println(liveLayoutList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void mainLiveList(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://share.egame.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LiveReqInterface reqInterface = retrofit.create(LiveReqInterface.class);
        String app_id = "lol";
        int page_num = 1;
        int page_size = 40;
        String param = "{\"key\":{\"module\":\"pgg_live_read_ifc_mt_svr\",\"method\":\"get_pc_live_list\",\"param\":{\"appid\":\""+app_id+"\",\"page_num\":"+page_num+",\"page_size\":"+page_size+",\"tag_id\":0,\"tag_id_str\":\"\"}}}";
        String appInfo = "{\"platform\":4,\"terminal_type\":2,\"egame_id\":\"egame_official\",\"version_code\":\"9.9.9.9\",\"version_name\":\"9.9.9.9\",\"ext_info\":{\"_qedj_t\":\"\",\"ALG-flag_type\":\"\",\"ALG-flag_pos\":\"\"}";
        Call<Fresponse> call = reqInterface.getLiveList(param,appInfo,"1");
        try{
            Response<Fresponse> response = call.execute();
            System.out.println(response.body());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
