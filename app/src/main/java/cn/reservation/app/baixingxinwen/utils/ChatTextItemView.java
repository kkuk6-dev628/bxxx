package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by naxing on 5/25/2018.
 */
public class ChatTextItemView extends LinearLayout {

    Context mContext;
    private CircleImageView mThumbnail;
    private CircleImageView emThumbnail;
    private TextView mName;
    private TextView emName;
    private TextView mTime;
    private TextView mDesc;
    private TextView emDesc;
    private Drawable mImgPlaceholder;

    public ChatTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ChatTextItemView(Context context, ChatTextItem chatItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_text_item, this, true);

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
        emThumbnail = (CircleImageView) findViewById(R.id.img_chat_me);
        if(chatItem.getmeThumbnail()!="") {
            Picasso
                    .with(mContext)
                    .load(chatItem.getmeThumbnail())
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 80), CommonUtils.getPixelValue(mContext, 80))
                    .into(emThumbnail);
        }
        mDesc = (TextView)findViewById(R.id.txt_chat_history_desc);
        mDesc.setText(chatItem.getmDesc());
        emDesc = (TextView)findViewById(R.id.txt_chat_me_desc);
        emDesc.setText(chatItem.getmeDesc());

        mTime = (TextView)findViewById(R.id.txt_chat_history_time);
        mTime.setText(chatItem.getmTime());

        mName = (TextView)findViewById(R.id.txt_chat_history_name);
        mName.setText(chatItem.getmName());

        emName = (TextView)findViewById(R.id.txt_chat_me_name);
        emName.setText(chatItem.getmeName());
        if(chatItem.getmName()=="" && chatItem.getmeName()!=""){
            LinearLayout lyt_img_chat = (LinearLayout) findViewById(R.id.lyt_img_chat);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.0f
            );
            lyt_img_chat.setLayoutParams(param);
            LinearLayout lyt_chat_friend = (LinearLayout) findViewById(R.id.lyt_chat_friend);
            param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.0f
            );
            lyt_chat_friend.setLayoutParams(param);
            LinearLayout lyt_img_chat_me = (LinearLayout) findViewById(R.id.lyt_img_chat_me);
            param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.7f
            );
            lyt_img_chat_me.setLayoutParams(param);
            LinearLayout lyt_chat_me_desc = (LinearLayout) findViewById(R.id.lyt_chat_me_desc);
            param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    3.3f
            );
            lyt_chat_me_desc.setLayoutParams(param);
        }else{
            LinearLayout lyt_img_chat = (LinearLayout) findViewById(R.id.lyt_img_chat);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.7f
            );
            lyt_img_chat.setLayoutParams(param);
            LinearLayout lyt_chat_friend = (LinearLayout) findViewById(R.id.lyt_chat_friend);
            param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    3.2f
            );
            lyt_chat_friend.setLayoutParams(param);

            RelativeLayout.LayoutParams layoutarams = (RelativeLayout.LayoutParams) mName.getLayoutParams();
            layoutarams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mName.setLayoutParams(layoutarams);
            LinearLayout lyt_img_chat_me = (LinearLayout) findViewById(R.id.lyt_img_chat_me);
            param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.0f
            );
            lyt_img_chat_me.setLayoutParams(param);
            LinearLayout lyt_chat_me_desc = (LinearLayout) findViewById(R.id.lyt_chat_me_desc);
            param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.0f
            );
            lyt_chat_me_desc.setLayoutParams(param);
        }
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
    public void setmeThumbnail(String thumbnail) {
        if(thumbnail!="") {
            Picasso
                    .with(mContext)
                    .load(thumbnail)
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 80), CommonUtils.getPixelValue(mContext, 80))
                    .into(emThumbnail);
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
    public void setmeName(String name) {
        emName.setText(name);
    }
    public void setmeDesc(String desc) {
        emDesc.setText(desc);
    }
}
