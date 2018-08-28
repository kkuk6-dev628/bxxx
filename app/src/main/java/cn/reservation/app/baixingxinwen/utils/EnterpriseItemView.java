package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.HomeActivity;

/**
 * Created by NSC on 6/5/2018.
 */
public class EnterpriseItemView extends LinearLayout {

    Context mContext;
    private ImageView mThumbnail;
    private TextView mName;
    private TextView mDesc;
    private TextView mPhone;
    private Drawable mImgPlaceholder;
    public EnterpriseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public EnterpriseItemView(Context context, final EnterpriseItem enterpriseItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.consult_item, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);

//        mThumbnail = (ImageView) findViewById(R.id.img_enterprise_logo);
//        if(enterpriseItem.getmThumbnail()!="") {
//            Picasso
//                    .with(mContext)
//                    .load(enterpriseItem.getmThumbnail())
//                    .placeholder(mImgPlaceholder)
//                    .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 82))
//                    .into(mThumbnail);
//        }
        mName = (TextView)findViewById(R.id.txt_enterprise_name);
        mName.setText(enterpriseItem.getmName());

        mDesc = (TextView)findViewById(R.id.txt_enterprise_desc);
        mDesc.setText(enterpriseItem.getmDesc());

        mPhone = (TextView)findViewById(R.id.txt_enterprise_phone);
        mPhone.setText(enterpriseItem.getmPhone());
        mPhone.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View view) {
                  System.out.println("tel:" + enterpriseItem.getmPhone());
                  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + enterpriseItem.getmPhone()));
                  HomeActivity.homeActivity.startActivity(intent);
              }
        });
        ((RelativeLayout)findViewById(R.id.rlt_enterprise_call)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("tel:" + enterpriseItem.getmPhone());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + enterpriseItem.getmPhone()));
                HomeActivity.homeActivity.startActivity(intent);
            }
        });
    }

    public void setThumbnail(String thumbnail) {
//        if(thumbnail!="") {
//            Picasso
//                    .with(mContext)
//                    .load(thumbnail)
//                    .placeholder(mImgPlaceholder)
//                    .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 82))
//                    .into(mThumbnail);
//        }
    }

    public void setName(String name) {
        mName.setText(name);
    }
    public void setDesc(String desc) {
        mDesc.setText(desc);
    }
    public void setPhone(String phone) {
        mPhone.setText(phone);
    }
}
