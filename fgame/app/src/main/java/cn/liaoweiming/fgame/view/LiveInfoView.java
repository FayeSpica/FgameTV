package cn.liaoweiming.fgame.view;

import android.content.Context;

import androidx.leanback.widget.ImageCardView;

import cn.liaoweiming.fgame.bean.LiveInfo;

public class LiveInfoView extends ImageCardView {
    private LiveInfo liveInfo;

    public LiveInfoView(Context context) {
        super(context);
    }

    public LiveInfo getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(LiveInfo liveInfo) {
        this.liveInfo = liveInfo;
    }
}
