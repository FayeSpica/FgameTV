package cn.liaoweiming.fgame.ijkplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import cn.liaoweiming.fgame.R;
import cn.liaoweiming.fgame.bean.DanmakuData;
import cn.liaoweiming.fgame.bean.DanmakuItem;
import cn.liaoweiming.fgame.bean.DanmakuReq;
import cn.liaoweiming.fgame.bean.LiveInfo;
import cn.liaoweiming.fgame.bean.Stream;
import cn.liaoweiming.fgame.danmaku.BiliDanmakuJsonParser;
import cn.liaoweiming.fgame.danmaku.DefaultDanmakuFilter;
import cn.liaoweiming.fgame.danmaku.JsonLoader;
import cn.liaoweiming.fgame.network.FgameService;
import cn.liaoweiming.fgame.util.LiveUtil;
import cn.liaoweiming.fgame.view.DoubleClickListener;
import cn.liaoweiming.fgame.view.GlideCircleWithBorder;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkPlayerActivity extends Activity implements SurfaceHolder.Callback , View.OnClickListener{
    IjkMediaPlayer ijkMediaPlayer;
    public static String TAG="IjkPlayerActivity";

    /** Danmaku Element*/
    private IDanmakuView mDanmakuView;
    private View mMediaController;
    private View mMediaControllerUp;
    private View mMediaControllerDown;
    public PopupWindow mPopupWindow;
    private Button mBtnRefresh;
    private WheelView mStreamListWheel;
    private Button mBtnHideDanmaku;
    private BaseDanmakuParser mParser;
    private DanmakuContext mContext;

    private ImageView liveAnchorImage;
    private TextView liveTitleText;
    private TextView liveAnchorText;
    private TextView liveOnlineText;

    SurfaceView surfaceView;

    private List<DanmakuItem> danmakuItemList;

    private LiveInfo liveInfo;
    private List<Stream> streamList;

    boolean activityFinish = false;
    /**-------------------- LifeCycle Implementation --------------------*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fgame_stream);
        initSurfaceView();
        initPlayer();
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ijkMediaPlayer != null ){
            ijkMediaPlayer.start();
        }
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(ijkMediaPlayer != null) {
            ijkMediaPlayer.pause();
        }
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (true){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        if(ijkMediaPlayer != null){
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
        if ( asyncDanmakuManagerTask!=null){
            asyncAddDanmakuListTaskFinish = true;
        }
        if( asyncAddDanmakuTask!=null){
            asyncAddDanmakuTaskFinish = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        if(ijkMediaPlayer != null){
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**-------------------- Ijkplayer Implementation --------------------*/
    private void initSurfaceView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
    }

    private void initPlayer(){
        Intent intent = getIntent();
        liveInfo=(LiveInfo) intent.getSerializableExtra("live_info");
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 8);

        ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L);

        ijkMediaPlayer.setOption(1, "probesize", 10240L);

        ijkMediaPlayer.setOption(1, "flush_packets", 1L);

        ijkMediaPlayer.setOption(4, "packet-buffering", 0L);

        ijkMediaPlayer.setOption(4, "framedrop", 5L);
        asyncGetStream(liveInfo.getAnchor_id());
    }

    private void refresh(){
        try {
            Log.d(TAG, "refresh");
            Toast.makeText(getApplicationContext(),"当前清晰度:"+streamList.get(mStreamListWheel.getCurrentItem()).getDesc(),Toast.LENGTH_LONG);
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
            ijkMediaPlayer = new IjkMediaPlayer();
            //asyncGetStream(liveInfo.getAnchor_id());
            ijkMediaPlayer.setDisplay(surfaceView.getHolder());
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.setDataSource(streamList.get(mStreamListWheel.getCurrentItem()).getPlayUrl());
            ijkMediaPlayer.prepareAsync();
            Log.i("initPlayer", "initPlayer");
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 8);

            ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L);

            ijkMediaPlayer.setOption(1, "probesize", 10240L);

            ijkMediaPlayer.setOption(1, "flush_packets", 1L);

            ijkMediaPlayer.setOption(4, "packet-buffering", 0L);

            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5L);
            ijkMediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void asyncGetStream(long roomId){

        new AsyncTask<Long,Void, List<Stream>>(){
            @Override
            protected List<Stream> doInBackground(Long... longs) {
                try {
                    Long id = longs[0];
                    List<Stream> list = FgameService.getInstance().getStreamListByAnchorId(id);
                    Log.d(TAG, "stream list = " + list);
                    return list;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Stream> list) {
                try{
                    if(list==null){
                        Toast.makeText(getApplicationContext(),"获取直播源失败",Toast.LENGTH_LONG);
                        return;
                    }
                    streamList = list;
                    mStreamListWheel.setTextSize(14);
                    mStreamListWheel.setTextColorCenter(Color.parseColor("#90FFFFFF"));
                    mStreamListWheel.setTextColorOut(Color.parseColor("#90FFFFFF"));
                    mStreamListWheel.setLineSpacingMultiplier(2f);
                    mStreamListWheel.setAdapter(new ArrayWheelAdapter(LiveUtil.getStreamStringList(streamList)));
                    mStreamListWheel.setCurrentItem(0);
                    mStreamListWheel.setCyclic(false);
                    mStreamListWheel.setOnClickListener(new DoubleClickListener() {
                        @Override
                        public void onDoubleClick(View v) {
                            Log.d(TAG,"mStreamListWheel onDoubleClick");
                            refresh();
                        }
                    });
                    mStreamListWheel.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            Log.d(TAG,"mStreamListWheel setOnKeyListener keyCode="+keyCode + " KeyEvent"+event.getKeyCode());
                            if((keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP||keyCode == KeyEvent.KEYCODE_DPAD_UP) &&event.getAction()==KeyEvent.ACTION_DOWN){
                                mStreamListWheel.setCurrentItem(mStreamListWheel.getCurrentItem()-1);
                            }
                            if((keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN||keyCode == KeyEvent.KEYCODE_DPAD_DOWN) &&event.getAction()==KeyEvent.ACTION_DOWN){
                                mStreamListWheel.setCurrentItem(mStreamListWheel.getCurrentItem()+1);
                            }
                            if((keyCode == KeyEvent.KEYCODE_ENTER||keyCode == KeyEvent.KEYCODE_BUTTON_A) &&event.getAction()==KeyEvent.ACTION_UP){
                                refresh();
                            }
                            return false;
                        }
                    });
                    mStreamListWheel.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(int index) {
                            Log.d(TAG,"mStreamListWheel onItemSelected index = "+index);
                        }
                    });
                    //mStreamListWheel.setDividerType(WheelView.DividerType.C);
                    ijkMediaPlayer.setDataSource(streamList.get(0).getPlayUrl());
                    ijkMediaPlayer.prepareAsync();
                    Log.i("initPlayer", "initPlayer");
                    ijkMediaPlayer.setScreenOnWhilePlaying(true);
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
                    ijkMediaPlayer.start();
                }catch (IOException e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
        }.execute(roomId);
    }

    /**-------------------- Danmaku Implementation --------------------*/

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        private Drawable mDrawable;

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {

                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if(drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = createSpannable(drawable);
                            danmaku.text = spannable;
                            if(mDanmakuView != null) {
                                mDanmakuView.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };

    private BaseDanmakuParser createParser(List<DanmakuItem> danmakuItemList) {
        JsonLoader loader = JsonLoader.instance();
        try {
            loader.load(danmakuItemList);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmakuJsonParser(danmakuItemList);
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    private void setLiveRoomInfo(){
        Glide.with(getApplicationContext())
                .load(liveInfo.getAnchor_face_url())
                .apply(RequestOptions.bitmapTransform(new GlideCircleWithBorder(this, 3, Color.parseColor("#ccffffff"))))//new RoundedCorners(80)))
                .into(liveAnchorImage);
        liveAnchorText.setText(liveInfo.getAnchor_name());
        liveTitleText.setText(liveInfo.getTitle());
        liveOnlineText.setText(LiveUtil.parseOnline(liveInfo));
    }

    private void hideMediaController(){
        if ( mMediaController.getVisibility()==View.GONE)
            return;
        TranslateAnimation hideAnimUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        TranslateAnimation hideAnimDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        hideAnimUp.setDuration(250);
        hideAnimDown.setDuration(250);
        mMediaControllerUp.startAnimation(hideAnimUp);
        mMediaControllerDown.startAnimation(hideAnimDown);
        hideAnimUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMediaController.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    Timer hideUITimer = new Timer();

    private void setHideUIDelay(int ms){
        hideUITimer.cancel();
        hideUITimer=null;
        hideUITimer = new Timer();
        hideUITimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideMediaController();
                    }
                });
            }
        }, ms);
    }

    private void showMediaControllerDelayed(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!ijkMediaPlayer.isPlaying()) {
                        Thread.sleep(200);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMediaController();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showMediaController(){
        if ( mMediaController.getVisibility()==View.VISIBLE)
            return;
        TranslateAnimation showAnimUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        TranslateAnimation showAnimDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        showAnimUp.setDuration(450);
        showAnimDown.setDuration(450);
        mMediaControllerUp.startAnimation(showAnimUp);
        mMediaControllerDown.startAnimation(showAnimDown);
        mMediaController.setVisibility(View.VISIBLE);
        mBtnRefresh.requestFocus();
        setHideUIDelay(4000);
    }

    private void findViews() {

        mMediaController = findViewById(R.id.media_controller);
        mMediaControllerUp = findViewById(R.id.media_controller_up);
        mMediaControllerDown = findViewById(R.id.media_controller_down);
        mBtnHideDanmaku = (Button) findViewById(R.id.btn_hide);
        mBtnRefresh = (Button) findViewById(R.id.btn_refresh);
        mStreamListWheel = (WheelView) findViewById(R.id.stream_list_wheel);
        mBtnHideDanmaku.setOnClickListener(this);
        mMediaController.setOnClickListener(this);
        mBtnRefresh.setOnClickListener(this);
        mStreamListWheel.setOnClickListener(this);

        mBtnRefresh.requestFocus();
        liveAnchorImage = findViewById(R.id.live_anchor_image);
        liveTitleText = findViewById(R.id.live_title_text);
        liveAnchorText = findViewById(R.id.live_anchor_text);
        liveOnlineText = findViewById(R.id.live_online_text);
        setLiveRoomInfo();
        showMediaControllerDelayed();

        //setHideUIDelay(4000);


        // DanmakuView

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 10); // 滚动弹幕最大显示10行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
        if (mDanmakuView != null) {
            danmakuItemList = new ArrayList<>();//service.getDanmaku(liveInfo.getAnchor_id(),service.getPidByAnchorId(liveInfo.getAnchor_id()), String.valueOf(System.currentTimeMillis()) );
            //mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mParser = createParser(danmakuItemList);
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                    //Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    //mMediaController.setVisibility(View.VISIBLE);
                    Log.d(TAG,"mMediaController.setVisibility(View.VISIBLE);");
                    showMediaController();
                    return false;
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            //mDanmakuView.showFPS(true);
            mDanmakuView.enableDanmakuDrawingCache(true);
            launchDammakuProvider();
        }
    }


    Timer timer = new Timer();

    LinkedBlockingQueue<DanmakuItem> danmakuItemLinkedBlockingQueue = new LinkedBlockingQueue<>();

    boolean asyncAddDanmakuTaskFinish = false;
    boolean asyncAddDanmakuListTaskFinish = false;

    class AsyncAddDanmakuTask extends Thread {
        @Override
        public void run() {
            DanmakuItem lastDanmaku = null;
            while (!asyncAddDanmakuTaskFinish){
                try {
                    //Log.d("AsyncAddDanmakuTask"," queue_size="+danmakuItemLinkedBlockingQueue.size());
                    final DanmakuItem danmakuItem = danmakuItemLinkedBlockingQueue.take();
                    //Log.d("AsyncAddDanmakuTask","addDanmaku danmakuItem="+danmakuItem);
                    //Log.d("AsyncAddDanmakuTask","addDanmaku queue_size="+danmakuItemLinkedBlockingQueue.size()+"danmakuItem"+danmakuItem);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDanmaku(danmakuItem);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class AsyncDanmakuManagerTask extends AsyncTask<DanmakuReq,Void,Void>{
        @Override
        protected Void doInBackground(DanmakuReq... danmakuReqs) {
            DanmakuReq danmakuReq = danmakuReqs[0];

            Long lastTm = Long.parseLong(danmakuReq.getLastTm())/1000;
            while (!asyncAddDanmakuListTaskFinish){
                DanmakuData danmakuData = FgameService.getInstance()
                        .getDanmakuData(danmakuReq.getAnchorId(),danmakuReq.getPid(),lastTm);
                List<DanmakuItem> danmakuItemList = danmakuData.getMsg_list();
                //Log.d("AsyncDanmakuManagerTask","获取弹幕数量："+danmakuItemList.size()+" 请求时间："+lastTm+" 最后时间："+danmakuData.getLast_tm());
                if (danmakuData != null && danmakuItemList != null) {
                    lastTm = danmakuData.getLast_tm();
                    for (DanmakuItem danmakuItem : danmakuItemList) {
                        try {
                            Long tm = danmakuItem.getTm() / 1000;
                            // filter
                            if (DefaultDanmakuFilter.isValid(danmakuItem)) {
                                danmakuItemLinkedBlockingQueue.put(danmakuItem);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    AsyncDanmakuManagerTask asyncDanmakuManagerTask;
    AsyncAddDanmakuTask asyncAddDanmakuTask;

    private void launchDammakuProvider(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DanmakuReq danmakuReq = new DanmakuReq(liveInfo.getAnchor_id(),FgameService.getInstance().getPidByAnchorId(liveInfo.getAnchor_id()),String.valueOf(System.currentTimeMillis()));
                asyncDanmakuManagerTask = new AsyncDanmakuManagerTask();
                asyncDanmakuManagerTask.execute(danmakuReq);
                asyncAddDanmakuTask = new AsyncAddDanmakuTask();
                asyncAddDanmakuTask.start();
            }
        }).start();
    }

    Random random = new Random();

    private void addDanmaku(DanmakuItem danmakuItem) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        danmaku.text = danmakuItem.getContent();
        danmaku.priority = 0;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 100 + random.nextInt(2000));
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = DefaultDanmakuFilter.getColor(danmakuItem);
        danmaku.textShadowColor = DefaultDanmakuFilter.getShadowColor(danmakuItem);
        mDanmakuView.addDanmaku(danmaku);
    }

    private void addDanmaku(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        danmaku.text = "这是一条弹幕" + System.nanoTime();
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;

        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
    }


    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 重置隐藏UI计时
        showMediaController();
        setHideUIDelay(4000);
        return super.onKeyUp(keyCode, event);
    }

    /**-------------------- Click Implementation --------------------*/



    @Override
    public void onClick(View v) {
        if(v == mBtnRefresh){
            if(mMediaController.getVisibility()==View.VISIBLE){
                refresh();
            }
        }
        if (v == mMediaController) {
            if(mMediaController.getVisibility()==View.VISIBLE){
                hideMediaController();
            }
        }
        if (mDanmakuView == null || !mDanmakuView.isPrepared())
            return;
        if (v == mBtnHideDanmaku) {
            if(mDanmakuView.isShown()){
                mBtnHideDanmaku.setText("显示弹幕");
                mDanmakuView.hide();
            }else {
                mBtnHideDanmaku.setText("隐藏弹幕");
                mDanmakuView.show();
            }
            // mPausedPosition = mDanmakuView.hideAndPauseDrawTask();
        } /*else if (v == mBtnShowDanmaku) {
            mDanmakuView.show();
            // mDanmakuView.showAndResumeDrawTask(mPausedPosition); // sync to the video time in your practice
        }*/
    }

    /**-------------------- Surface Implementation --------------------*/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ijkMediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mDanmakuView.getConfig().setDanmakuMargin(20);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mDanmakuView.getConfig().setDanmakuMargin(40);
        }
    }
}
