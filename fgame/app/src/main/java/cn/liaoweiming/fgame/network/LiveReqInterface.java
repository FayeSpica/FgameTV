package cn.liaoweiming.fgame.network;

import cn.liaoweiming.fgame.bean.fgame.Fresponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LiveReqInterface {

    @GET("https://egame.qq.com/{anchor_id}")  // 获取anchor_id房间的pid
    Call<ResponseBody>  getPid(@Path("anchor_id")Long anchorId);

    @GET("http://wdanmaku.egame.qq.com/cgi-bin/pgg_barrage_async_fcgi") // 通过anchor_id pid请求弹幕
    Call<Fresponse> getDanmakuList(@Query("param") String param, @Query("app_info") String appInfo, @Query("tt") String tt);

    @GET("https://share.egame.qq.com/cgi-bin/pgg_live_async_fcgi") // 请求appid类直播房间列表
    Call<Fresponse> getLiveList(@Query("param") String param, @Query("app_info") String appInfo, @Query("tt") String tt);

    @GET("https://egame.qq.com/gamelist") // 获取所有直播分类
    Call<ResponseBody> getLayoutList();
}
