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
 * Created by naxing on 5/25/2018.
 */
public class SearchItemView extends LinearLayout {

    Context mContext;
    private ImageView mThumbnail;
    private ImageView mAdver;
    private TextView mTitle01;
    private TextView mTitle02;
    private TextView mTitle03;
    private TextView mProperty01;
    private TextView mProperty02;
    private TextView mProperty03;
    private TextView mDesc;
    private TextView mPrice;
    private Drawable mImgPlaceholder;
    private ImageView mTop;
    //private LinearLayout mContent;
    //private LinearLayout mAdverContent;
    public SearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SearchItemView(Context context, SearchItem searchItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_item, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);

        mThumbnail = (ImageView) findViewById(R.id.img_home_favor_thumbnail);
        mAdver = (ImageView) findViewById(R.id.img_adver_thumbnail);
        //mContent = (LinearLayout) findViewById(R.id.lyt_home_favor_thumbnail);
        //mAdverContent = (LinearLayout) findViewById(R.id.lyt_adver_thumbnail);
        LinearLayout lyt_img_thumbnail=(LinearLayout)findViewById(R.id.lyt_img_thumbnail);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.4f
        );
        lyt_img_thumbnail.setLayoutParams(params);
        LinearLayout lyt_home_favor_desc=(LinearLayout)findViewById(R.id.lyt_home_favor_desc);
        params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2.6f
        );
        lyt_home_favor_desc.setLayoutParams(params);
        System.out.println("searchItem.getmAdver()+"+searchItem.getmAdver());
        System.out.println("searchItem.getmAdver()++"+searchItem.getmThumbnail());
        if(!searchItem.getmAdver().equals("")){
            mAdver.setVisibility(VISIBLE);
            mThumbnail.setVisibility(GONE);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_home_favor_desc.setLayoutParams(params);
            if(!searchItem.getmThumbnail().equals("")) {
                Picasso
                        .with(mContext)
                        .load(searchItem.getmThumbnail())
                        .placeholder(mImgPlaceholder)
                        .resize(CommonUtils.getPixelValue(mContext, 130), CommonUtils.getPixelValue(mContext, 80))
                        .into(mAdver);
            }
            Picasso
                    .with(mContext)
                    .load(searchItem.getmAdver())
                    .placeholder(mImgPlaceholder)
                    .fit()
                    .into(mThumbnail);
        }else{ //if((searchItem.getmFid().equals("2") && (searchItem.getmSortid().equals("1") || searchItem.getmSortid().equals("4"))) || (searchItem.getmFid().equals("93") && searchItem.getmSortid().equals("33")) || (searchItem.getmFid().equals("39") && searchItem.getmSortid().equals("58")) || (searchItem.getmFid().equals("39") && searchItem.getmSortid().equals("2")) || (searchItem.getmFid().equals("40") && searchItem.getmSortid().equals("3")) || (searchItem.getmFid().equals("92")) || (searchItem.getmFid().equals("50") && searchItem.getmSortid().equals("61"))){
            mAdver.setVisibility(GONE);
            mThumbnail.setVisibility(VISIBLE);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.4f
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2.6f
            );
            lyt_home_favor_desc.setLayoutParams(params);
            if(!searchItem.getmThumbnail().equals("")) {
                Picasso
                        .with(mContext)
                        .load(searchItem.getmThumbnail())
                        .placeholder(mImgPlaceholder)
                        .resize(CommonUtils.getPixelValue(mContext, 130), CommonUtils.getPixelValue(mContext, 80))
                        .into(mThumbnail);
            }
        }
        mDesc = (TextView)findViewById(R.id.txt_home_favor_desc);
        mDesc.setText(searchItem.getmDesc());

        mPrice = (TextView)findViewById(R.id.txt_home_favor_price);
        mPrice.setText(searchItem.getmPrice());

        mTitle01 = (TextView)findViewById(R.id.txt_property1);
        mTitle02 = (TextView)findViewById(R.id.txt_property2);
        mTitle03 = (TextView)findViewById(R.id.txt_property3);
        mProperty01 = (TextView)findViewById(R.id.txt_property_val1);
        mProperty02 = (TextView)findViewById(R.id.txt_property_val2);
        mProperty03 = (TextView)findViewById(R.id.txt_property_val3);
        mTop = (ImageView)findViewById(R.id.img_news_top);
        switch (searchItem.getmPostState()){
            case "0":
                mTop.setVisibility(INVISIBLE);
                break;
            case "1":
                mTop.setVisibility(VISIBLE);
                break;
        }
    }

    public void setThumbnail(String thumbnail, String mFid, String mSortid, String mAdverUrl) {
        LinearLayout lyt_img_thumbnail=(LinearLayout)findViewById(R.id.lyt_img_thumbnail);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        LinearLayout lyt_home_favor_desc=(LinearLayout)findViewById(R.id.lyt_home_favor_desc);
        mAdver.setVisibility(GONE);
        mThumbnail.setVisibility(VISIBLE);
        if(!mAdverUrl.equals("")) {
            //mContent.setVisibility(GONE);
            //mAdverContent.setVisibility(VISIBLE);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_home_favor_desc.setLayoutParams(params);
            mAdver.setVisibility(VISIBLE);
            mThumbnail.setVisibility(GONE);
            Picasso
                    .with(mContext)
                    .load(mAdverUrl)
                    .placeholder(mImgPlaceholder)
                    .resize(512, 0)
                    .into(mAdver);
        }else{ //if((mFid.equals("2") && (mSortid.equals("1") || mSortid.equals("4"))) || (mFid.equals("93") && mSortid.equals("33")) || mFid.equals("39") || (mFid.equals("40") && mSortid.equals("3")) || (mFid.equals("92")) || (mFid.equals("50") && mSortid.equals("61"))){
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.4f
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2.6f
            );
            lyt_home_favor_desc.setLayoutParams(params);
            if(!thumbnail.equals("")) {
                Picasso
                        .with(mContext)
                        .load(thumbnail)
                        .placeholder(mImgPlaceholder)
                        .resize(CommonUtils.getPixelValue(mContext, 130), CommonUtils.getPixelValue(mContext, 80))
                        .into(mThumbnail);
            }
        }/*else{
            lyt_img_thumbnail=(LinearLayout)findViewById(R.id.lyt_img_thumbnail);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_img_thumbnail.setLayoutParams(params);
            lyt_home_favor_desc=(LinearLayout)findViewById(R.id.lyt_home_favor_desc);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_home_favor_desc.setLayoutParams(params);
        }
        */
    }

    public void setTitle01(String title01) {
        mTitle01.setText(title01);
    }
    public void setTitle02(String title02) {
        mTitle02.setText(title02);
    }
    public void setTitle03(String title03) {
        mTitle03.setText(title03);
    }
    public void setProperty01(String property01) {
        mProperty01.setText(property01);
    }
    public void setProperty02(String property02) {
        mProperty02.setText(property02);
    }
    public void setProperty03(String property03) {
        mProperty03.setText(property03);
    }
    public void setDesc(String desc) {
        mDesc.setText(desc);
    }
    public void setPrice(String price) {
        mPrice.setText(price);
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
    public void setAdver(String thumbnail){
        System.out.println("advert:"+thumbnail);
        if(!thumbnail.equals("")) {
            //mContent.setVisibility(GONE);
            //mAdverContent.setVisibility(VISIBLE);
            LinearLayout lyt_img_thumbnail=(LinearLayout)findViewById(R.id.lyt_img_thumbnail);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_img_thumbnail.setLayoutParams(params);
            LinearLayout lyt_home_favor_desc=(LinearLayout)findViewById(R.id.lyt_home_favor_desc);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_home_favor_desc.setLayoutParams(params);
            Picasso
                    .with(mContext)
                    .load(thumbnail)
                    .placeholder(mImgPlaceholder)
                    .fit()
                    .into(mThumbnail);
        }else {
            //mContent.setVisibility(VISIBLE);
            //mAdverContent.setVisibility(GONE);
            LinearLayout lyt_img_thumbnail=(LinearLayout)findViewById(R.id.lyt_img_thumbnail);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.4f
            );
            lyt_img_thumbnail.setLayoutParams(params);
            LinearLayout lyt_home_favor_desc=(LinearLayout)findViewById(R.id.lyt_home_favor_desc);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2.6f
            );
            lyt_home_favor_desc.setLayoutParams(params);
        }
    }
}
