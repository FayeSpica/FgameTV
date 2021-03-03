package cn.liaoweiming.fgame.presenter;

import android.content.Context;

import cn.liaoweiming.fgame.bean.LiveInfo;

public class TextCardPresenter extends AbstractCardPresenter<TextCardView> {

    public TextCardPresenter(Context context) {
        super(context);
    }

    @Override
    protected TextCardView onCreateView() {
        return new TextCardView(getContext());
    }

    @Override
    public void onBindViewHolder(LiveInfo liveInfo, TextCardView cardView) {
        cardView.updateUi(liveInfo);
    }

}