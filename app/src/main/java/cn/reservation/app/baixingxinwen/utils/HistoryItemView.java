package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/13/2017.
 */
public class HistoryItemView extends LinearLayout {

    Context mContext;
    private ImageView mThumbnail;
    private TextView mDate;
    private TextView mDesc;
    private TextView mPrice;
    private Drawable mImgPlaceholder;
    private CheckBox mChk;
    private LinearLayout mLytChk;
    private ImageView mTop;
    private LinearLayout lyt_room_favor_thumbnail;

    public HistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public HistoryItemView(Context context, final HistoryItem historyItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.history_item, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);

        mThumbnail = (ImageView) findViewById(R.id.img_favor_thumbnail);
        if(historyItem.getmThumbnail()!="") {
            Picasso
                    .with(mContext)
                    .load(historyItem.getmThumbnail())
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 82))
                    .into(mThumbnail);
        }
        mDate = (TextView)findViewById(R.id.txt_room_favor_date);
        mDate.setText(historyItem.getmDate());

        mDesc = (TextView)findViewById(R.id.txt_room_favor_desc);
        mDesc.setText(historyItem.getmDesc());

        mPrice = (TextView)findViewById(R.id.txt_room_favor_price);
        mPrice.setText(historyItem.getmPrice());
        mChk = (CheckBox)findViewById(R.id.img_select);
        mChk.setChecked(historyItem.getmChk());
        mLytChk = (LinearLayout) findViewById(R.id.lyt_select);
        mTop = (ImageView)findViewById(R.id.img_news_top);
        switch (historyItem.getmPostState()){
            case "0":
                mTop.setVisibility(INVISIBLE);
                break;
            case "1":
                mTop.setVisibility(VISIBLE);
                break;
        }
    }

    public void setThumbnail(String thumbnail) {
        if(thumbnail!="") {
            Picasso
                    .with(mContext)
                    .load(thumbnail)
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 82))
                    .into(mThumbnail);
        }
    }

    public void setDate(String date) {
        mDate.setText(date);
    }
    public void setDesc(String desc) {
        mDesc.setText(desc);
    }
    public void setPrice(String price) {
        mPrice.setText(price);
    }
    public void setChk(boolean chk){
        mChk.setChecked(chk);
    }
    public CheckBox getChk(){
        return mChk;
    }
    public void setChkPanel(boolean chkPanel){
        if(chkPanel==true){
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );
            mLytChk.setLayoutParams(param1);
        }else{
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT
            );
            mLytChk.setLayoutParams(param1);
        }
    }
    public void setPostState(String postState) {
        switch(postState){
            case "0":
                mTop.setVisibility(INVISIBLE);
                break;
            case "1":
                mTop.setVisibility(VISIBLE);
                break;
            default:
                mTop.setVisibility(INVISIBLE);
                break;
        }
    }
}
