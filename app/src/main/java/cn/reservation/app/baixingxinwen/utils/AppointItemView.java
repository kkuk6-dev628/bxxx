package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/19/2017.
 */
public class AppointItemView extends LinearLayout {

    private Context mContext;
    private TextView mTxtDateTime;
    private TextView mTxtHospital;
    private TextView mTxtPatient;

    public AppointItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public AppointItemView(Context context) {
        super(context);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.appoint_item, this, true);

        mTxtDateTime = (TextView) findViewById(R.id.txt_alarm_date_time);
        mTxtHospital = (TextView) findViewById(R.id.txt_alarm_hospital);
        mTxtPatient = (TextView) findViewById(R.id.txt_alarm_patient);
    }

    public void setDateTime(String date, String hour, String minute) {
        String d = CommonUtils.getFormattedDateTime(mContext, date, hour, minute);
        mTxtDateTime.setText(d);
    }

    public void setHospital(String hospital, String doctor, String room) {
        mTxtHospital.setText(hospital + " / " + doctor + " " + room);
    }

    public void setPatient(String patient) {
        mTxtPatient.setText(mContext.getString(R.string.patient_name) + patient);
    }
}
