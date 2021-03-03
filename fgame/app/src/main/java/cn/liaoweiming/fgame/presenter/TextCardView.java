package cn.liaoweiming.fgame.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.leanback.widget.BaseCardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import cn.liaoweiming.fgame.R;
import cn.liaoweiming.fgame.bean.LiveInfo;
import cn.liaoweiming.fgame.util.LiveUtil;
import cn.liaoweiming.fgame.view.GlideCircleWithBorder;

public class TextCardView extends BaseCardView {

    public TextCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_icon_card, this);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void updateUi(LiveInfo liveInfo) {
        ImageView mainImageView = findViewById(R.id.main_image);
        TextView avatarText = findViewById(R.id.avatar_text);
        final ImageView avatarImageView = findViewById(R.id.avatar_image);
        TextView footText = findViewById(R.id.footer_text);

        TextView title_text = findViewById(R.id.title_text);

        title_text.setText(liveInfo.getTitle());
        Glide.with(getContext())
                .load(liveInfo.getVideo_info().getUrl())
                .into(mainImageView);

        avatarText.setText(liveInfo.getAnchor_name());
        Glide.with(getContext())
                .load(liveInfo.getAnchor_face_url())
                .apply(RequestOptions.bitmapTransform(new GlideCircleWithBorder(getContext(), 1, Color.parseColor("#ccffffff"))))//new RoundedCorners(36)))
                .into(avatarImageView);
        footText.setText(LiveUtil.parseOnline(liveInfo.getOnline()));

        // Create a rounded drawable.
        /*
        int resourceId = card.getLocalImageResourceId(getContext());
        Bitmap bitmap = BitmapFactory
                .decodeResource(getContext().getResources(), resourceId);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
        drawable.setAntiAlias(true);
        drawable.setCornerRadius(
                Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        imageView.setImageDrawable(drawable);*/
    }

}