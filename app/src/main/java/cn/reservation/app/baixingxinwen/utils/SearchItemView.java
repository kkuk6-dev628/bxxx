package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.LoginActivity;
import cn.reservation.app.baixingxinwen.activity.SearchActivity;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private ImageView mImgProperty01;
    private ImageView mImgProperty02;
    private ImageView mImgProperty03;
    private TextView mDesc;
    private TextView mPrice;
    private TextView mPostTime;
    private TextView mType;
    private Drawable mImgPlaceholder;
    private ImageView mTop;
    private ImageView mPhone;

    private SearchItem search_item;

    //private LinearLayout mContent;
    //private LinearLayout mAdverContent;
    public SearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SearchItemView(Context context, final SearchItem searchItem) {
        super(context);

        mContext = context;
        search_item = searchItem;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_item_save, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);

        mThumbnail = (ImageView) findViewById(R.id.img_home_favor_thumbnail);
        mAdver = (ImageView) findViewById(R.id.img_adver_thumbnail);
        //mContent = (LinearLayout) findViewById(R.id.lyt_home_favor_thumbnail);
        //mAdverContent = (LinearLayout) findViewById(R.id.lyt_adver_thumbnail);
        LinearLayout lyt_img_thumbnail = (LinearLayout) findViewById(R.id.lyt_img_thumbnail);
        LayoutParams params = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                1.3f
        );
        lyt_img_thumbnail.setLayoutParams(params);
        LinearLayout lyt_home_favor_desc = (LinearLayout) findViewById(R.id.lyt_home_favor_desc);
        params = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                2.7f
        );
        lyt_home_favor_desc.setLayoutParams(params);
        System.out.println("searchItem.getmAdver()+" + searchItem.getmAdver());
        System.out.println("searchItem.getmAdver()++" + searchItem.getmThumbnail());

        CircleImageView circleImage = findViewById(R.id.img_circle);
        if (searchItem.getmFid().equals("48")) {
            circleImage.setVisibility(VISIBLE);
            mThumbnail.setVisibility(GONE);
        }
        else{
            circleImage.setVisibility(GONE);
            mThumbnail.setVisibility(VISIBLE);
        }

        String defaultImageName = searchItem.getmDefaultImageName();
        if (!searchItem.getmAdver().equals("")) {
            // 광고인경우 화상만 나와야 하므로
            mAdver.setVisibility(VISIBLE);
            mThumbnail.setVisibility(GONE);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_home_favor_desc.setLayoutParams(params);

            Picasso
                    .with(mContext)
                    .load(searchItem.getmAdver())
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 320), 0)
                    .into(mAdver);
        } else { //if((searchItem.getmFid().equals("2") && (searchItem.getmSortid().equals("1") || searchItem.getmSortid().equals("4"))) || (searchItem.getmFid().equals("93") && searchItem.getmSortid().equals("33")) || (searchItem.getmFid().equals("39") && searchItem.getmSortid().equals("58")) || (searchItem.getmFid().equals("39") && searchItem.getmSortid().equals("2")) || (searchItem.getmFid().equals("40") && searchItem.getmSortid().equals("3")) || (searchItem.getmFid().equals("92")) || (searchItem.getmFid().equals("50") && searchItem.getmSortid().equals("61"))){
            mAdver.setVisibility(GONE);
            mThumbnail.setVisibility(VISIBLE);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    1.3f
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    2.7f
            );
            lyt_home_favor_desc.setLayoutParams(params);
            if (searchItem.isMarriedPage()) {
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1
                );
                lyt_img_thumbnail.setLayoutParams(params);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        3
                );
                lyt_home_favor_desc.setLayoutParams(params);
            }
            if (!searchItem.getmThumbnail().equals("")) {
                // 사용자가 upload 한 화상이 있는 경우
                if (searchItem.getmFid().equals("48")) {
                    Picasso
                            .with(mContext)
                            .load(searchItem.getmThumbnail())
                            .placeholder(mImgPlaceholder)
                            .centerCrop()
                            .resize(CommonUtils.getPixelValue(mContext, 120), CommonUtils.getPixelValue(mContext, 80))
                            .into(circleImage);
                }
                else{
                    Picasso
                            .with(mContext)
                            .load(searchItem.getmThumbnail())
                            .placeholder(mImgPlaceholder)
                            .centerCrop()
                            .resize(CommonUtils.getPixelValue(mContext, 120), CommonUtils.getPixelValue(mContext, 80))
                            .into(mThumbnail);
                }
            } else {
                // 사용자가 upload 한 화상이 없는 경우
                // 이때 대치화상이 있는 종류라면 대치화상을 넣어야 한다.
                if (!defaultImageName.equals("")) {

                    Resources res = getResources();
                    int resID = res.getIdentifier(defaultImageName, "drawable", "cn.reservation.app.baixingxinwen");
                    Drawable drawable = res.getDrawable(resID);

                    if (searchItem.getmFid().equals("48")) {
                        circleImage.setImageDrawable(drawable);
                    }
                    else {
                        mThumbnail.setImageDrawable(drawable);
                    }
                }
            }

            //화상이 없어야 하는 종류일때 즉 구직정보와 같은것
            if (searchItem.ismIsNoImage()) {
                mThumbnail.setVisibility(ImageView.GONE);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        0
                );
                lyt_img_thumbnail.setLayoutParams(params);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        4
                );
                lyt_home_favor_desc.setLayoutParams(params);
            }
        }


        mDesc = (TextView) findViewById(R.id.txt_home_favor_desc);
        mDesc.setText(searchItem.getmDesc());

        mPrice = (TextView) findViewById(R.id.txt_home_favor_price);
        mPrice.setText(searchItem.getmPrice());

        mType = (TextView) findViewById(R.id.item_type);
        mType.setText(searchItem.getmTypeVal());
//        if(searchItem.getmTypeVal().equals(""))
//            mType.setVisibility(TextView.GONE);
//        else
//            mType.setText(searchItem.getmTypeVal());
        mPostTime = (TextView) findViewById(R.id.post_time);
        mPostTime.setText(searchItem.getmPostTime());

        mPhone = (ImageView) findViewById(R.id.img_phone);
        if (searchItem.getmPhoneNumber().equals("")) {
            LinearLayout lyt_tel_time = (LinearLayout) findViewById(R.id.lyt_tel_time);
            lyt_tel_time.setPadding(0, CommonUtils.getPixelValue(mContext, 65), 0, 0);
            mPhone.setVisibility(ImageView.GONE);
        } else {
            mPhone.setVisibility(ImageView.VISIBLE);
            mPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CommonUtils.isLogin) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + search_item.getmPhoneNumber()));
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("from_activity", "room_detail");

                        SearchActivity activity = (SearchActivity) mContext;
                        activity.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                        activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                }
            });
        }


        mImgProperty01 = (ImageView) findViewById(R.id.img_property1);
        if (searchItem.ismHasImgProperty1())
            mImgProperty01.setVisibility(ImageView.VISIBLE);

        mImgProperty02 = (ImageView) findViewById(R.id.img_property2);
        if (searchItem.ismHasImgProperty2())
            mImgProperty02.setVisibility(ImageView.VISIBLE);

        mImgProperty03 = (ImageView) findViewById(R.id.img_property3);
        if (searchItem.ismHasImgProperty3())
            mImgProperty03.setVisibility(ImageView.VISIBLE);

//        mTitle01 = (TextView)findViewById(R.id.txt_property1);
//        mTitle02 = (TextView)findViewById(R.id.txt_property2);
//        mTitle03 = (TextView)findViewById(R.id.txt_property3);
        mProperty01 = (TextView) findViewById(R.id.txt_property_val1);
        mProperty01.setText(searchItem.getmProperty01());

        mProperty02 = (TextView) findViewById(R.id.txt_property_val2);
        mProperty02.setText(searchItem.getmProperty02());

        mProperty03 = (TextView) findViewById(R.id.txt_property_val3);
        mProperty03.setText(searchItem.getmProperty03());

        mTop = (ImageView) findViewById(R.id.img_news_top);
//        switch (searchItem.getmPostState()){
//            case "0":
//                mTop.setVisibility(INVISIBLE);
//                break;
//            case "1":
//                mTop.setVisibility(VISIBLE);
//                break;
//        }
    }

    public void setThumbnail(String thumbnail, String mFid, String mSortid, String mAdverUrl) {
        LinearLayout lyt_img_thumbnail = (LinearLayout) findViewById(R.id.lyt_img_thumbnail);
        LayoutParams params;
        LinearLayout lyt_home_favor_desc = (LinearLayout) findViewById(R.id.lyt_home_favor_desc);
        CircleImageView circleImage = findViewById(R.id.img_circle);
        mAdver.setVisibility(GONE);

        if (mFid.equals("48")) {
            circleImage.setVisibility(VISIBLE);
            mThumbnail.setVisibility(GONE);
        }
        else{
            circleImage.setVisibility(GONE);
            mThumbnail.setVisibility(VISIBLE);
        }

        String defaultImageName = search_item.getmDefaultImageName();
        if (!mAdverUrl.equals("")) {
            //광고인 경우
            //mContent.setVisibility(GONE);
            //mAdverContent.setVisibility(VISIBLE);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_img_thumbnail.setLayoutParams(params);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_home_favor_desc.setLayoutParams(params);
            mAdver.setVisibility(VISIBLE);
            mThumbnail.setVisibility(GONE);
            Picasso
                    .with(mContext)
                    .load(mAdverUrl)
                    .placeholder(mImgPlaceholder)
                    .resize(CommonUtils.getPixelValue(mContext, 320), 0)
                    .into(mAdver);
        } else { //if((mFid.equals("2") && (mSortid.equals("1") || mSortid.equals("4"))) || (mFid.equals("93") && mSortid.equals("33")) || mFid.equals("39") || (mFid.equals("40") && mSortid.equals("3")) || (mFid.equals("92")) || (mFid.equals("50") && mSortid.equals("61"))){
            if (search_item.ismIsNoImage()) {
                mThumbnail.setVisibility(ImageView.GONE);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        0
                );
                lyt_img_thumbnail.setLayoutParams(params);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        4
                );
                lyt_home_favor_desc.setLayoutParams(params);
            } else {
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1.3f
                );
                lyt_img_thumbnail.setLayoutParams(params);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        2.7f
                );
                lyt_home_favor_desc.setLayoutParams(params);
            }

            if (search_item.isMarriedPage()) {
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1
                );
                lyt_img_thumbnail.setLayoutParams(params);
                params = new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        3
                );
                lyt_home_favor_desc.setLayoutParams(params);
            }

            if (!thumbnail.equals("")) {
                // 사용자가 upload 한 화상이 있는경우
                if (mFid.equals("48")) {
                    Picasso
                            .with(mContext)
                            .load(thumbnail)
                            .placeholder(mImgPlaceholder)
                            .centerCrop()
                            .resize(CommonUtils.getPixelValue(mContext, 120), CommonUtils.getPixelValue(mContext, 80))
                            .into(circleImage);
                }
                else{
                    Picasso
                            .with(mContext)
                            .load(thumbnail)
                            .placeholder(mImgPlaceholder)
                            .centerCrop()
                            .resize(CommonUtils.getPixelValue(mContext, 120), CommonUtils.getPixelValue(mContext, 80))
                            .into(mThumbnail);
                }
            } else {

                if (!defaultImageName.equals("")) {
                    //// 사용자가 upload 한 화상이 없는 경우
                    // 대치화상이 있으면 그화상을 놓아야 한다.

                    Resources res = getResources();
                    int resID = res.getIdentifier(defaultImageName, "drawable", "cn.reservation.app.baixingxinwen");
                    Drawable drawable = res.getDrawable(resID);
                    if (mFid.equals("48")) {
                        circleImage.setImageDrawable(drawable);
                    }
                    else {
                        mThumbnail.setImageDrawable(drawable);
                    }
                }
            }
        }

        /*else{
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
//        mTitle01.setText(title01);
    }

    public void setTitle02(String title02) {
//        mTitle02.setText(title02);
    }

    public void setTitle03(String title03) {
//        mTitle03.setText(title03);
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
        mTop.setVisibility(INVISIBLE);
//        switch(postState){
//            case "0":
//                mTop.setVisibility(INVISIBLE);
//                break;
//            case "1":
//                mTop.setVisibility(VISIBLE);
//                break;
//            default:
//                mTop.setVisibility(INVISIBLE);
//                break;
//        }
    }

    public void setAdver(String thumbnail) {
        System.out.println("advert:" + thumbnail);
        if (!thumbnail.equals("")) {
            //mContent.setVisibility(GONE);
            //mAdverContent.setVisibility(VISIBLE);
            LinearLayout lyt_img_thumbnail = (LinearLayout) findViewById(R.id.lyt_img_thumbnail);
            LayoutParams params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    4
            );
            lyt_img_thumbnail.setLayoutParams(params);
            LinearLayout lyt_home_favor_desc = (LinearLayout) findViewById(R.id.lyt_home_favor_desc);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    0
            );
            lyt_home_favor_desc.setLayoutParams(params);
            Picasso
                    .with(mContext)
                    .load(thumbnail)
                    .placeholder(mImgPlaceholder)
                    .fit()
                    .into(mThumbnail);
        } else {
            //mContent.setVisibility(VISIBLE);
            //mAdverContent.setVisibility(GONE);
            LinearLayout lyt_img_thumbnail = (LinearLayout) findViewById(R.id.lyt_img_thumbnail);
            LayoutParams params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    1.4f
            );
            lyt_img_thumbnail.setLayoutParams(params);
            LinearLayout lyt_home_favor_desc = (LinearLayout) findViewById(R.id.lyt_home_favor_desc);
            params = new LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    2.6f
            );
            lyt_home_favor_desc.setLayoutParams(params);
        }
    }

    private int getDefaultDrawableBySotID(String sortid) {
        return R.drawable.bianmin1;
    }

    public void setData(SearchItem searchItem) {
        search_item = searchItem;
        mType.setText(searchItem.getmTypeVal());
        mPostTime.setText(searchItem.getmPostTime());
        if (searchItem.getmPhoneNumber().equals(""))
            mPhone.setVisibility(ImageView.GONE);
        else
            mPhone.setVisibility(ImageView.VISIBLE);
        if (searchItem.ismHasImgProperty1())
            mImgProperty01.setVisibility(ImageView.VISIBLE);

        if (searchItem.ismHasImgProperty2())
            mImgProperty02.setVisibility(ImageView.VISIBLE);

        if (searchItem.ismHasImgProperty3())
            mImgProperty03.setVisibility(ImageView.VISIBLE);

        mProperty01.setText(searchItem.getmProperty01());

        mProperty02.setText(searchItem.getmProperty02());

        mProperty03.setText(searchItem.getmProperty03());

//        switch (searchItem.getmPostState()){
//            case "0":
//                mTop.setVisibility(INVISIBLE);
//                break;
//            case "1":
//                mTop.setVisibility(VISIBLE);
//                break;
//        }
    }
}
