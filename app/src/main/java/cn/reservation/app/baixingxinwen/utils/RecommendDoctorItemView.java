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
 * Created by LiYin on 3/20/2017.
 */
public class RecommendDoctorItemView extends LinearLayout {
    private Context mContext;
    private ImageView mDoctorPhoto;
    private TextView mTxtDoctorName;
    private TextView mTxtRoom;
    private TextView mTxtDoctorDegree;
    private TextView mTxtHospital;
    private TextView mTxtDoctorFee;
    private Drawable mImgPlaceholder;

    public RecommendDoctorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public RecommendDoctorItemView(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.recommend_doctor_item, this, true);

        mDoctorPhoto = (ImageView) findViewById(R.id.img_doctor_photo);

        mTxtDoctorName = (TextView)findViewById(R.id.txt_doctor_name);
        mTxtRoom = (TextView)findViewById(R.id.txt_doctor_room);
        mTxtDoctorDegree = (TextView)findViewById(R.id.txt_doctor_degree);
        mTxtHospital = (TextView)findViewById(R.id.txt_hospital);
        mTxtDoctorFee = (TextView)findViewById(R.id.txt_doctor_fee);
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);
    }

    public void setDoctorPhoto(String photo) {
        Picasso
                .with(mContext)
                .load(photo)
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(mDoctorPhoto);
    }

    public void setDoctorName(String name) {
        mTxtDoctorName.setText(name);
    }

    public void setRoom(String room) {
        mTxtRoom.setText(room);
    }

    public void setDoctorDegree(String doctorDegree) {
        mTxtDoctorDegree.setText(doctorDegree);
    }

    public void setHospital(String hospital) {
        mTxtHospital.setText(hospital);
    }

    public void setDoctorFee(String doctorFee) {
        mTxtDoctorFee.setText(doctorFee);
    }
}
