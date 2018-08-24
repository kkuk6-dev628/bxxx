package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/15/2017.
 */
public class DoctorItemView extends LinearLayout {
    Context mContext;
    private ImageView mPhoto;
    private TextView mName;
    private TextView mHospital;
    private TextView mRoom;
    private TextView mDegree;
    private TextView mFee;
    private LinearLayout mLayoutHospital;
    private Drawable mImgPlaceholder;

    public DoctorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DoctorItemView(Context context, DoctorItem doctorItem) {
        super(context);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.doctor_item, this, true);

        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);

        mPhoto = (ImageView) findViewById(R.id.img_doctor_photo);

        Picasso
                .with(mContext)
                .load(doctorItem.getmPhoto())
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(mPhoto);

        mName = (TextView)findViewById(R.id.txt_doctor_name);
        mName.setText(doctorItem.getmName());

        mRoom = (TextView)findViewById(R.id.txt_doctor_room);
        mLayoutHospital = (LinearLayout) findViewById(R.id.lyt_hospital);
        mHospital = (TextView)findViewById(R.id.txt_hospital);
        mDegree = (TextView)findViewById(R.id.txt_doctor_degree);

        if (doctorItem.isDoctor()) {
            mRoom.setText(doctorItem.getmRoom());
            mDegree.setText(doctorItem.getmDegree());
        } else {
            mLayoutHospital.setVisibility(View.VISIBLE);
            mHospital.setText(doctorItem.getmHospital());
            mRoom.setVisibility(View.GONE);
            mDegree.setVisibility(View.GONE);
        }

        mFee = (TextView)findViewById(R.id.txt_doctor_fee);
        mFee.setText(doctorItem.getmFee());
    }

    public void setPhoto(String photo) {
        Picasso
                .with(mContext)
                .load(photo)
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(mPhoto);
    }

    public void setDoctorName(String name) {
        mName.setText(name);
    }

    public void setHospital(String hospital, boolean isDoctor) {
        if (isDoctor){
            mLayoutHospital.setVisibility(View.GONE);
        } else {
            mLayoutHospital.setVisibility(View.VISIBLE);
            mHospital.setText(hospital);
        }
    }

    public void setRoom(String room, boolean isDoctor) {
        if (isDoctor) {
            mRoom.setVisibility(View.VISIBLE);
            mRoom.setText(room);
        } else {
            mRoom.setVisibility(View.GONE);
        }
    }

    public void setDegree(String degree, boolean isDoctor) {
        if (isDoctor) {
            mDegree.setVisibility(View.VISIBLE);
            mDegree.setText(degree);
        } else {
            mDegree.setVisibility(View.GONE);
        }
    }

    public void setFee(String fee) {
        mFee.setText(fee);
    }
}
