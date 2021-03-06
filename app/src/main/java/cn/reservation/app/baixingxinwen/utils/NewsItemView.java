package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/14/2017.
 */
public class NewsItemView extends LinearLayout {

    Context mContext;
    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mStartTime;
    private TextView mTopTime;
    private TextView mEndTime;
    private TextView mViewCnt;
    private TextView mViewState;
    private String mStatus;
    private Drawable mImgPlaceholder;
    private RelativeLayout rlt_news_btn1;
    private RelativeLayout rlt_news_btn2;
    private RelativeLayout rlt_news_btn3;
    private RelativeLayout rlt_news_btn4;
    private RelativeLayout rlt_news_desc1;
    private TextView txt_btn3;
    private TextView m_news_state;
    private ImageView mTop;

    public NewsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public NewsItemView(Context context, NewsItem newsItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.news_item1, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);

        mThumbnail = (ImageView) findViewById(R.id.img_news_favor_thumbnail);
        if(newsItem.getmImage()!="") {
            Picasso
                    .with(mContext)
                    .load(newsItem.getmImage())
                    .placeholder(mImgPlaceholder)
                    .resize(256, 0)
                    .into(mThumbnail);
        }
        mTitle = (TextView)findViewById(R.id.txt_my_news_title);
        mTitle.setText(newsItem.getmTitle());

        mStartTime = (TextView)findViewById(R.id.txt_my_news_start);
        mStartTime.setText(newsItem.getmStartTime());

        mEndTime = (TextView)findViewById(R.id.txt_my_news_end);
        mEndTime.setText(newsItem.getmEndTime());

        mTopTime = (TextView)findViewById(R.id.txt_my_news_top);
        mTopTime.setText(newsItem.getmTopTime());

        mViewCnt = (TextView)findViewById(R.id.txt_my_news_views);
        mViewCnt.setText(newsItem.getmViewCnt());

        rlt_news_btn1 = (RelativeLayout)findViewById(R.id.rlt_news_btn1);
        rlt_news_btn2 = (RelativeLayout)findViewById(R.id.rlt_news_btn2);
        rlt_news_btn3 = (RelativeLayout)findViewById(R.id.rlt_news_btn3);
        rlt_news_btn4 = (RelativeLayout)findViewById(R.id.rlt_news_btn4);
        rlt_news_desc1 = (RelativeLayout)findViewById(R.id.rlt_news_desc1);
        m_news_state = (TextView)findViewById(R.id.txt_charge_price_top);
        txt_btn3 = (TextView)findViewById(R.id.txt_my_news_share);
        mTop = (ImageView)findViewById(R.id.img_news_top);
        switch (newsItem.getmPostState()){
            case "0":
                if(TextUtils.equals(newsItem.getmPaid(),"1")){
                    m_news_state.setText("?????????");
                    rlt_news_desc1.setVisibility(INVISIBLE);
                }else{
                    rlt_news_desc1.setVisibility(INVISIBLE);
                }
                mTop.setVisibility(INVISIBLE);
                mEndTime.setVisibility(GONE);
                break;
            case "1":
                rlt_news_desc1.setVisibility(INVISIBLE);
                m_news_state.setText("?????????");
                mTop.setVisibility(VISIBLE);
                mEndTime.setVisibility(VISIBLE);
                break;
        }
        switch(newsItem.getmStatus()){
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                rlt_news_btn1.setVisibility(View.VISIBLE);
                rlt_news_btn2.setVisibility(View.VISIBLE);
                rlt_news_btn3.setVisibility(View.VISIBLE);
                rlt_news_btn4.setVisibility(View.VISIBLE);
                mViewState.setText("?????????");
                txt_btn3.setText("??????");
                //mTop.setVisibility(View.INVISIBLE);
                mViewState.setTextColor(getResources().getColor(R.color.colorComment));
                break;
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                rlt_news_btn1.setVisibility(View.VISIBLE);
                rlt_news_btn2.setVisibility(View.VISIBLE);
                rlt_news_btn3.setVisibility(View.VISIBLE);
                rlt_news_btn4.setVisibility(View.VISIBLE);
                mViewState.setText("?????????");
                txt_btn3.setText("??????");
                //mTop.setVisibility(View.INVISIBLE);
                mViewState.setTextColor(getResources().getColor(R.color.colorComment));
                break;
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                rlt_news_btn1.setVisibility(View.INVISIBLE);
                rlt_news_btn2.setVisibility(View.INVISIBLE);
                rlt_news_btn3.setVisibility(View.VISIBLE);
                rlt_news_btn4.setVisibility(View.VISIBLE);
                mViewState.setText("?????????");
                txt_btn3.setText("??????");
                mViewState.setTextColor(getResources().getColor(R.color.colorConfirmText));
                break;
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                rlt_news_btn1.setVisibility(View.INVISIBLE);
                rlt_news_btn2.setVisibility(View.INVISIBLE);
                rlt_news_btn3.setVisibility(View.VISIBLE);
                rlt_news_btn4.setVisibility(View.INVISIBLE);
                txt_btn3.setText("????????????");
                mViewState.setTextColor(getResources().getColor(R.color.colorConfirmText));
                mViewState.setText("?????????");
                break;
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                rlt_news_btn1.setVisibility(View.INVISIBLE);
                rlt_news_btn2.setVisibility(View.INVISIBLE);
                rlt_news_btn3.setVisibility(View.VISIBLE);
                rlt_news_btn4.setVisibility(View.VISIBLE);
                txt_btn3.setText("????????????");
                mViewState.setTextColor(getResources().getColor(R.color.colorConfirmText));
                mViewState.setText("?????????");
                break;
            default:
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                rlt_news_btn1.setVisibility(View.INVISIBLE);
                rlt_news_btn2.setVisibility(View.INVISIBLE);
                rlt_news_btn3.setVisibility(View.VISIBLE);
                rlt_news_btn4.setVisibility(View.VISIBLE);
                txt_btn3.setText("?????????");
                mViewState.setTextColor(getResources().getColor(R.color.colorComment));
                mViewState.setText("");
                break;
        }


    }

    public void setThumbnail(String thumbnail) {
        if(thumbnail!="") {
            Picasso
                    .with(mContext)
                    .load(thumbnail)
                    .placeholder(mImgPlaceholder)
                    .resize(256, 0)
                    .into(mThumbnail);
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
    public void setStartIime(String start) {
        mStartTime.setText(start);
    }
    public void setEndTime(String end) { mEndTime.setText(end);    }
    public void setmTopTime(String top) { mTopTime.setText(top);}
    public void setViewCnt(String cnt) {
        mViewCnt.setText(cnt);
    }
    public void setState(String State) {
        switch(State){
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                mViewState.setText("?????????");
                break;
            case "?????????":
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                mViewState.setText("?????????");
                break;
            default:
                mViewState =(TextView)findViewById(R.id.txt_my_news_state);
                mViewState.setText("");
                break;
        }
    }
    public void setPostState(String postState, String postPaid) {
        switch(postState){
            case "0":
                if(TextUtils.equals(postPaid,"1")){
                    m_news_state.setText("?????????");
                    rlt_news_desc1.setVisibility(INVISIBLE);
                }else{
                    rlt_news_desc1.setVisibility(INVISIBLE);
                }
                mTop.setVisibility(INVISIBLE);
                mEndTime.setVisibility(GONE);
                break;
            case "1":
                m_news_state.setText("?????????");
                rlt_news_desc1.setVisibility(INVISIBLE);
                mTop.setVisibility(VISIBLE);
                mEndTime.setVisibility(VISIBLE);
                break;
        }
    }
}
