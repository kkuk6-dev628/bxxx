package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.MonthDateListAdapter;
import cn.reservation.app.baixingxinwen.adapter.OtherDoctorListAdapter;
import cn.reservation.app.baixingxinwen.adapter.TimesItemListAdapter;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.MonthDateItem;

public class ChooseAppointRoomActivity extends ChooseAppointActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_appoint_room);

        res = getResources();
        mContext = ChooseAppointRoomActivity.this;

        isForDoctor = false;
        Intent intent = getIntent();
        mDoctorItem = (DoctorItem) intent.getSerializableExtra("Doctor");

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.choose_appoint));

        TextView txtRoomName = (TextView) findViewById(R.id.txt_room_name);
        txtRoomName.setText(mDoctorItem.getmName());
        TextView txtHospitalName = (TextView) findViewById(R.id.txt_hospital_name);
        txtHospitalName.setText(mDoctorItem.getmHospital());
        TextView txtRoomFee = (TextView) findViewById(R.id.txt_room_fee);
        txtRoomFee.setText(mDoctorItem.getmFee());
        ImageView imgDoctorPhoto = (ImageView) findViewById(R.id.img_doctor_photo);
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);
        Picasso
                .with(mContext)
                .load(mDoctorItem.getmPhoto())
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(imgDoctorPhoto);

        mTxtCurrentYear = (TextView) findViewById(R.id.txt_current_year);
        mTxtCurrentMonth = (TextView) findViewById(R.id.txt_current_month);
        mTxtCurrentDay = (TextView) findViewById(R.id.txt_current_day);
        mTxtCurrentWeekDay = (TextView) findViewById(R.id.txt_current_weekday);
        setCurrentDate();

        LinearLayout lytCurrentDate = (LinearLayout) findViewById(R.id.lyt_current_date);
        lytCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        mLstTimes = (ListView) findViewById(R.id.lst_times);
        mTimesItemListAdapter = new TimesItemListAdapter(mContext, ChooseAppointRoomActivity.this);
        mLstTimes.setAdapter(mTimesItemListAdapter);
        mLstTimes.setOnTouchListener(new ListViewTouchListener());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHlvMonthDate = (RecyclerView) findViewById(R.id.hlv_month_date);
        mHlvMonthDate.setLayoutManager(layoutManager);

        mMonthDateItems.add(new MonthDateItem(false, 1, 1, 1, 1, false, false));
        mMonthDateListAdapter = new MonthDateListAdapter(mMonthDateItems, mContext, ChooseAppointRoomActivity.this);
        mHlvMonthDate.setAdapter(mMonthDateListAdapter);
        setMonthDays(mYear, mMonth, mDay);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHlvOtherDoctors = (RecyclerView) findViewById(R.id.hlv_other_doctors);
        mHlvOtherDoctors.setLayoutManager(layoutManager2);

        mOtherDoctorItems.add(new DoctorItem(0, 0, 0, " ", "", "", "", "", "0", "", 0, false));
        mOtherDoctorListAdapter = new OtherDoctorListAdapter(mOtherDoctorItems, mContext, ChooseAppointRoomActivity.this);
        mHlvOtherDoctors.setAdapter(mOtherDoctorListAdapter);
        //loadOtherDoctors();

        TextView btnSummary = (TextView) findViewById(R.id.btn_view_summary);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAppointRoomActivity.this, SummaryViewActivity.class);
                intent.putExtra("Doctor", mDoctorItem);
                ChooseAppointRoomActivity.this.startActivity(intent);
                ChooseAppointRoomActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });
    }

    @Override
    public void setAppoint(String time) {
        Intent intent = new Intent(ChooseAppointRoomActivity.this, AppointInfoViewActivity.class);
        intent.putExtra("Year", mYear);
        intent.putExtra("Month", mMonth);
        intent.putExtra("Day", mDay);
        intent.putExtra("Weekday", mWeekDay);
        intent.putExtra("Time", time);
        intent.putExtra("Doctor", mDoctorItem);
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public void selectOtherDoctor(int position) {
        Intent intent = new Intent(ChooseAppointRoomActivity.this, ChooseAppointDoctorActivity.class);
        DoctorItem doctorItem = mOtherDoctorListAdapter.getItem(position);
        intent.putExtra("Doctor", doctorItem);
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }
}
