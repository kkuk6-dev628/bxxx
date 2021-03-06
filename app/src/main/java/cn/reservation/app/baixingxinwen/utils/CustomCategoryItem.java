package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.SearchActivity;

/**
 * Created by LiYin on 3/20/2017.
 */
public class CustomCategoryItem extends LinearLayout implements View.OnClickListener {

    private ImageView mImageView;
    private TextView mTxtTitle;
    private Resources res;
    private Context mContext;
    public AnimatedActivity pActivity;
    private JSONObject jsonData;
    RelativeLayout rltMainClickable;

    public CustomCategoryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCategoryItem(Context context, JSONObject item, AnimatedActivity pActivity1) {
        super(context);
//        res = context.getResources();

        mContext = context;

        pActivity = pActivity1;
        jsonData = item;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_category_item, this, true);

        this.setOrientation(LinearLayout.HORIZONTAL);
//        this.setWeightSum((float) 1.5);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.FILL_PARENT);
        params.width = 0;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.weight = (float) 0.5;
        params.setMargins(1,1,1,1);
        this.setLayoutParams(params);


//        mTxtTitle = (TextView) findViewById(R.id.txt_category);
//        mImageView = (ImageView) findViewById(R.id.img_category_view);

//        LinearLayout rl = (LinearLayout) getChildAt(0);
        rltMainClickable = (RelativeLayout) getChildAt(0);
        rltMainClickable.setOnClickListener(this);
        
        mTxtTitle = (TextView) rltMainClickable.getChildAt(1);
        mImageView = (ImageView) rltMainClickable.getChildAt(0);

        if(item != null){
            String title = item.optString("item_label");
            mTxtTitle.setText(title);

            String imgLocalUrl = item.optString("imageLocalFilePath");
            if(imgLocalUrl != null){
                File imgFile = new  File(imgLocalUrl);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mImageView.setImageBitmap(myBitmap);
                }
            }
            else{
                mImageView.setImageResource(R.drawable.new_wait_red);
            }
        }
        else{
            String t = "DEFAULT";
            mTxtTitle.setText(t);
            mImageView.setImageResource(R.drawable.new_wait_red);
        }


    }

    public void setInformation(Bitmap bmp, String title){
        mImageView.setImageBitmap(bmp);
        mTxtTitle.setText(title);
    }

    public ImageView getmImageView() {
        return mImageView;
    }

    public TextView getmTxtTitle() {
        return mTxtTitle;
    }

    public void setJsonData(JSONObject jsonDataIn){
        jsonData = jsonDataIn;
    }

    @Override
    public void onClick(View view) {
        String title = (String) this.mTxtTitle.getText();
        
        if(jsonData == null)
            return;

        String item_name = jsonData.optString("item_name");
        String item_label = jsonData.optString("item_label");
        String item_fid = jsonData.optString("item_fid");
        String item_sortid = jsonData.optString("item_storeid1");
        if(item_name == null || item_name.equals(""))
            return;

        Intent intent;
        rltMainClickable.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rltMainClickable.setClickable(true);
            }
        }, 1000);

//        Intent intent = new Intent(this.mContext, SearchActivity.class);
//        intent.putExtra("name", item_name);
//        intent.putExtra("label", item_label);
//        intent.putExtra("fid", item_fid);
//        intent.putExtra("sortid", item_sortid);
//        pActivity.startChildActivity("activity_search", intent);


        switch (item_name){
            case "room"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "1");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "tax"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "2");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "house"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "3");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "invite"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "4");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "history"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "5");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "service"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "6");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "car"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "7");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "old"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "8");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "dazhe"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "9");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "join"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "10");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "anniver"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "11");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "educate"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "12");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "call"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "13");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "outland"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "14");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "animal"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "15");
                pActivity.startChildActivity("activity_search", intent);
                break;
            case "travel"://????????????
                intent = new Intent(this.mContext, SearchActivity.class);
                intent.putExtra("PostItem", "16");
                pActivity.startChildActivity("activity_search", intent);
                break;

//        }
//        else if (title == R.id.btn_home_waitred){//????????????
//            intent = new Intent(this.mContext, SearchActivity.class);
//        }
//            case "????????????"://????????????
//                intent = new Intent(this.mContext, SearchActivity.class);
//                break;
//            case "????????????-????????????"://????????????-????????????
//                intent = new Intent(this.mContext, EnterpriseListActivity.class);
//                intent.putExtra("enterprise", "????????????");
//                pActivity.startChildActivity("activity_enterprise", intent);
//                break;
//            case "????????????-??????"://????????????-??????
//                intent = new Intent(this.mContext, EnterpriseListActivity.class);
//                intent.putExtra("enterprise", "??????");
//                pActivity.startChildActivity("activity_enterprise", intent);
//                break;
//            case "????????????-??????"://????????????-??????
//                intent = new Intent(this.mContext, EnterpriseListActivity.class);
//                intent.putExtra("enterprise", "??????");
//                pActivity.startChildActivity("activity_enterprise", intent);
//                break;
//            case "??????????????????"://??????????????????
//                intent = new Intent(this.mContext, FullSearchActivity.class);
//                pActivity.startChildActivity("full_search", intent);
//                break;
            default:
                break;
        }
    }

    //    public void setSelected(boolean selected) {
//        if (selected) {
//            mTxtTitle.setTextColor(res.getColor(R.color.colorPrimaryText));
//        } else {
//            mTxtTitle.setTextColor(res.getColor(R.color.colorTabText));
//        }
//    }

}
