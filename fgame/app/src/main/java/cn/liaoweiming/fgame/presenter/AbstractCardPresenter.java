package cn.liaoweiming.fgame.presenter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.Presenter;

import cn.liaoweiming.fgame.bean.LiveInfo;

public abstract class AbstractCardPresenter<T extends BaseCardView> extends Presenter {

    private static final String TAG = "AbstractCardPresenter";
    private final Context mContext;

    /**
     * @param context The current context.
     */
    public AbstractCardPresenter(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override public final ViewHolder onCreateViewHolder(ViewGroup parent) {
        T cardView = onCreateView();
        return new ViewHolder(cardView);
    }

    @Override public final void onBindViewHolder(ViewHolder viewHolder, Object item) {
        LiveInfo liveInfo = (LiveInfo) item;
        onBindViewHolder(liveInfo, (T) viewHolder.view);
    }

    @Override public final void onUnbindViewHolder(ViewHolder viewHolder) {
        onUnbindViewHolder((T) viewHolder.view);
    }

    public void onUnbindViewHolder(T cardView) {
        // Nothing to clean up. Override if necessary.
    }

    /**
     * Invoked when a new view is created.
     *
     * @return Returns the newly created view.
     */
    protected abstract T onCreateView();

    /**
     * Implement this method to update your card's view with the data bound to it.
     *
     * @param liveInfo The model containing the data for the card.
     * @param cardView The view the card is bound to.
     * @see Presenter#onBindViewHolder(Presenter.ViewHolder, Object)
     */
    public abstract void onBindViewHolder(LiveInfo liveInfo, T cardView);

}