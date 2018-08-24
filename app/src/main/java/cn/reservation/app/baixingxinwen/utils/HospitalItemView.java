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
 * Created by LiYin on 3/13/2017.
 */
public class HospitalItemView extends LinearLayout {

    Context mContext;
    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mDesc;
    private TextView mRegion;
    private Drawable mImgPlaceholder;

    public HospitalItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public HospitalItemView(Context context, HospitalItem hospitalItem) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hospital_item, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_hospital);

        mThumbnail = (ImageView) findViewById(R.id.img_hospital_thumbnail);
        Picasso
                .with(mContext)
                .load(hospitalItem.getmThumbnail())
                .placeholder(mImgPlaceholder)
                .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 82))
                .into(mThumbnail);

        mTitle = (TextView)findViewById(R.id.txt_hospital_title);
        mTitle.setText(hospitalItem.getmTitle());

        mDesc = (TextView)findViewById(R.id.txt_hospital_desc);
        mDesc.setText(hospitalItem.getmDesc());

        mRegion = (TextView)findViewById(R.id.txt_hospital_region);
        mRegion.setText(hospitalItem.getmRegion());
    }

    public void setThumbnail(String thumbnail) {
        Picasso
                .with(mContext)
                .load(thumbnail)
                .placeholder(mImgPlaceholder)
                .resize(CommonUtils.getPixelValue(mContext, 122), CommonUtils.getPixelValue(mContext, 82))
                .into(mThumbnail);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
    public void setDesc(String desc) {
        mDesc.setText(desc);
    }
    public void setRegion(String region) {
        mRegion.setText(region);
    }
}
