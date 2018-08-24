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
public class AppointHistoryItemView extends LinearLayout {

    private Context mContext;
    private ImageView mImgDoctorPhoto;
    private TextView mTxtHospital;
    private TextView mTxtDoctorName;
    private TextView mTxtRoom;
    private TextView mTxtDate;
    private TextView mTxtPatient;
    public TextView btnAppoint;
    private Drawable mImgPlaceholder;

    public AppointHistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public AppointHistoryItemView(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.appoint_history_item, this, true);

        mImgDoctorPhoto = (ImageView) findViewById(R.id.img_doctor_photo);
        mTxtHospital = (TextView) findViewById(R.id.txt_hospital_title);
        mTxtDoctorName = (TextView) findViewById(R.id.txt_doctor_name);
        mTxtRoom = (TextView) findViewById(R.id.txt_room_title);
        mTxtPatient = (TextView) findViewById(R.id.txt_patient);
        mTxtDate = (TextView) findViewById(R.id.txt_date);
        btnAppoint = (TextView) findViewById(R.id.btn_appoint);
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);
    }

    public void setImgDoctorPhoto(String photo) {
        Picasso
                .with(mContext)
                .load(photo)
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(mImgDoctorPhoto);
    }

    public void setHospital(String hospital) {
        mTxtHospital.setText(hospital);
    }

    public void setDoctorName(String doctorName) {
        mTxtDoctorName.setText(doctorName);
    }

    public void setRoom(String room) {
        mTxtRoom.setText(room);
    }

    public void setPatient(String patient) {
        mTxtPatient.setText(patient);
    }

    public void setDate(String date, String hour, String minute) {
        String d = CommonUtils.getFormattedDate(mContext, date);
        mTxtDate.setText(d);
    }
}
