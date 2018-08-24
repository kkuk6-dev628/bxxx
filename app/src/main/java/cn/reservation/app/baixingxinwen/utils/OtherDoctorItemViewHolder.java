package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/16/2017.
 */
public class OtherDoctorItemViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private TextView mTxtName;
    private ImageView mImgPhoto;
    public RelativeLayout mLayoutOtherDoctorPhoto;
    private Drawable mImgPlaceholder;

    public OtherDoctorItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mLayoutOtherDoctorPhoto = (RelativeLayout) itemView.findViewById(R.id.lyt_other_doctor_photo);
        mTxtName = (TextView) itemView.findViewById(R.id.txt_other_doctor_name);
        mImgPhoto = (ImageView) itemView.findViewById(R.id.img_other_doctor_photo);
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);
    }

    public void setName(String name) {
        mTxtName.setText(name);
    }

    public void setPhoto(String photo) {
        Picasso
                .with(mContext)
                .load(photo)
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(mImgPhoto);
    }

    public void setLayoutParams(int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width, height);
        mLayoutOtherDoctorPhoto.setLayoutParams(layoutParams);
    }
}
