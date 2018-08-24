package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/20/2017.
 */
public class HealthArticleItemView extends LinearLayout{
    private Context mContext;
    private ImageView mImgThumbnail;
    private TextView mTxtTitle;
    private TextView mTxtContent;
    private Drawable mImgPlaceholder;

    public HealthArticleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
    public HealthArticleItemView(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.health_article_item, this, true);

        mImgThumbnail = (ImageView) findViewById(R.id.img_article_thumbnail);
        mTxtTitle = (TextView) findViewById(R.id.txt_article_title);
        mTxtContent = (TextView) findViewById(R.id.txt_article_content);
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_hospital);
    }

    public void setThumbnail(String map) {
        Picasso
                .with(mContext)
                .load(map)
                .placeholder(mImgPlaceholder)
                .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 81))
                .into(mImgThumbnail);
    }

    public void setTitle(String title) {
        mTxtTitle.setText(title);
    }

    public void setContent(String content) {
        mTxtContent.setText(content);
    }

}
