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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by naxing on 5/25/2018.
 */
public class ChatItemView extends LinearLayout {

    Context mContext;
    private ImageView mThumbnail;
    private TextView mName;
    private TextView mTime;
    private TextView mDesc;
    private Drawable mImgPlaceholder;

    public ChatItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ChatItemView(Context context, ChatItem chatItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_item, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);

        mThumbnail = (CircleImageView) findViewById(R.id.img_chat_friend);
        if(chatItem.getmThumbnail()!="") {
            Picasso
                    .with(mContext)
                    .load(chatItem.getmThumbnail())
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 80), CommonUtils.getPixelValue(mContext, 80))
                    .into(mThumbnail);
        }
        mDesc = (TextView)findViewById(R.id.txt_chat_history_desc);
        mDesc.setText(chatItem.getmDesc());

        mTime = (TextView)findViewById(R.id.txt_chat_history_time);
        mTime.setText(chatItem.getmTime());

        mName = (TextView)findViewById(R.id.txt_chat_history_name);
        mName.setText(chatItem.getmName());
    }

    public void setThumbnail(String thumbnail) {
        if(thumbnail!="") {
            Picasso
                    .with(mContext)
                    .load(thumbnail)
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 80), CommonUtils.getPixelValue(mContext, 80))
                    .into(mThumbnail);
        }
    }
    public void setTime(String time) {
        mTime.setText(time);
    }
    public void setName(String name) {
        mName.setText(name);
    }
    public void setDesc(String desc) {
        mDesc.setText(desc);
    }
}
