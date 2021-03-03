/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package cn.liaoweiming.fgame;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.liaoweiming.fgame.bean.LiveInfo;
import cn.liaoweiming.fgame.bean.LiveInfoReq;
import cn.liaoweiming.fgame.bean.LiveLayout;
import cn.liaoweiming.fgame.ijkplayer.IjkPlayerActivity;
import cn.liaoweiming.fgame.network.FgameService;
import cn.liaoweiming.fgame.presenter.TextCardPresenter;
import cn.liaoweiming.fgame.view.GlideCircleWithBorder;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    private List<ArrayObjectAdapter> arrayObjectAdapterList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    boolean getLiveListFinish = false;

    private void loadRows() {
       // List<Movie> list = MovieList.setupMovies();
        Log.d(TAG, "loadRows");
        new AsyncTask<Void,Void,List<LiveLayout>>(){
            @Override
            protected List<LiveLayout> doInBackground(Void... voids) {
                List<LiveLayout> liveLayoutList = FgameService.getInstance().getLayoutList();
                return liveLayoutList;
            }

            @Override
            protected void onPostExecute(List<LiveLayout> liveLayoutList) {
                //List<LiveInfo> liveInfoList = fgameService1.getLiveListByAppid("lol",1);
                ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
                //CardPresenter cardPresenter = new CardPresenter();
                //ImageCardViewPresenter cardPresenter = new ImageCardViewPresenter(getActivity(),R.style.GameCardTheme);
                //arrayObjectAdapterList = new ArrayList<>(Collections.nCopies(liveLayoutList.size(),new ArrayObjectAdapter(cardPresenter)));
                TextCardPresenter cardPresenter = new TextCardPresenter(getActivity());
                arrayObjectAdapterList = new ArrayList<>(liveLayoutList.size());
                int i;
                for (i = 0; i < liveLayoutList.size(); i++) {
                    arrayObjectAdapterList.add(new ArrayObjectAdapter(cardPresenter));
                    new AsyncTask<LiveInfoReq,Void,List<LiveInfo>>(){
                        private int i;
                        @Override
                        protected List<LiveInfo> doInBackground(LiveInfoReq... liveInfoReqs) {
                            if (getLiveListFinish)
                                return new ArrayList<>();
                            LiveInfoReq liveInfoReq = liveInfoReqs[0];
                            this.i = liveInfoReq.getI();
                            Log.d("AsyncTask LiveInfoReq",liveInfoReq.toString());
                            List<LiveInfo> liveInfoList = FgameService.getInstance().getLiveListByAppId(liveInfoReq.getAppid(),liveInfoReq.getPageNum(),liveInfoReq.getPageSize());
                            return liveInfoList;
                        }

                        @Override
                        protected void onPostExecute(List<LiveInfo> liveInfos) {
                            super.onPostExecute(liveInfos);
                            for(LiveInfo liveInfo : liveInfos) {
                                //liveInfo
                                arrayObjectAdapterList.get(i).add(liveInfo);
                                Log.d("LiveInfoReq","arrayObjectAdapterList "+i);
                            }
                        }
                    }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,new LiveInfoReq(liveLayoutList.get(i),1,10,i));
                    //for (int j = 0; j < liveInfoList.size(); j++) {
                    //arrayObjectAdapterList.get(i).add(liveInfoList.get(j));
                    //}
                    HeaderItem header = new HeaderItem(i, liveLayoutList.get(i).getTitle());
                    rowsAdapter.add(new ListRow(header, arrayObjectAdapterList.get(i)));
                }

                HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");

                GridItemPresenter mGridPresenter = new GridItemPresenter();
                ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
                gridRowAdapter.add(getResources().getString(R.string.grid_view));
                gridRowAdapter.add(getString(R.string.error_fragment));
                gridRowAdapter.add(getResources().getString(R.string.personal_settings));
                rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

                setAdapter(rowsAdapter);
            }
        }.execute();

    }



    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

        Glide.with(getActivity())
                .load(uri)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mBackgroundManager.setDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof LiveInfo) {
                LiveInfo liveInfo = (LiveInfo) item;
                Log.d(TAG, "Item: " + item.toString());
                /*
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);
                */
                Intent intent = new Intent(getActivity(), IjkPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("live_info",liveInfo);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof LiveInfo) {
                mBackgroundUri = ((LiveInfo) item).getVideo_info().getUrl();
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        getLiveListFinish = true;
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        getLiveListFinish = false;
        Log.d(TAG,"onStart");
    }
}
