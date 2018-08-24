package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/16/2017.
 */
public class TimesItemView extends LinearLayout {
    public Context mContext;
    private TextView mtxtTime;
    private TextView mbtnAppoint;

    public TimesItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public TimesItemView(Context context, TimesItem timesItem) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.times_item, this, true);

        mtxtTime = (TextView)findViewById(R.id.txt_time);
        mtxtTime.setText(timesItem.getmTime());
        if (timesItem.ismActive()) {
            mtxtTime.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
        } else {
            mtxtTime.setTextColor(mContext.getResources().getColor(R.color.colorRestDate));
        }

        mbtnAppoint = (TextView)findViewById(R.id.btn_appoint);
        if (timesItem.ismActive()) {
            mbtnAppoint.setText(mContext.getString(R.string.possible_appoint));
            mbtnAppoint.setTextColor(mContext.getResources().getColor(R.color.colorPossibleStatus));
            mbtnAppoint.setBackground(mContext.getResources().getDrawable(R.drawable.btn_appoint_selector));
        } else {
            mbtnAppoint.setText(mContext.getString(R.string.impossible_appoint));
            mbtnAppoint.setTextColor(mContext.getResources().getColor(R.color.colorRestDate));
            mbtnAppoint.setBackground(mContext.getResources().getDrawable(R.drawable.btn_impossible_appoint_shape));
        }
    }

    public void setTxtTime(String time, boolean isActive) {
        mtxtTime.setText(time);
        if (isActive) {
            mtxtTime.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
        } else {
            mtxtTime.setTextColor(mContext.getResources().getColor(R.color.colorRestDate));
        }
    }

    public void setBtnAppoint(boolean isActive) {
        if (isActive) {
            mbtnAppoint.setText(mContext.getString(R.string.possible_appoint));
            mbtnAppoint.setTextColor(mContext.getResources().getColor(R.color.colorPossibleStatus));
            mbtnAppoint.setBackground(mContext.getResources().getDrawable(R.drawable.btn_appoint_selector));
        } else {
            mbtnAppoint.setText(mContext.getString(R.string.impossible_appoint));
            mbtnAppoint.setTextColor(mContext.getResources().getColor(R.color.colorRestDate));
            mbtnAppoint.setBackground(mContext.getResources().getDrawable(R.drawable.btn_impossible_appoint_shape));
        }
    }
}
