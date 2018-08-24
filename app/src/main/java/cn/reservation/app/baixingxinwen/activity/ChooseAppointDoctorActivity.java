package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.MonthDateListAdapter;
import cn.reservation.app.baixingxinwen.adapter.OtherDoctorListAdapter;
import cn.reservation.app.baixingxinwen.adapter.TimesItemListAdapter;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.EndlessRecyclerViewScrollListener;
import cn.reservation.app.baixingxinwen.utils.MonthDateItem;

public class ChooseAppointDoctorActivity extends ChooseAppointActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_appoint_doctor);

        res = getResources();
        mContext = ChooseAppointDoctorActivity.this;

        Intent intent = getIntent();
        mDoctorItem = (DoctorItem) intent.getSerializableExtra("Doctor");
        mPatientID = intent.getStringExtra("PatientID");
        isExpert = intent.getBooleanExtra("isExpert", false);
        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.choose_appoint));

        TextView txtDoctorName = (TextView) findViewById(R.id.txt_doctor_name);
        txtDoctorName.setText(mDoctorItem.getmName());
        TextView txtDoctorRoom = (TextView) findViewById(R.id.txt_doctor_room);
        txtDoctorRoom.setText(mDoctorItem.getmRoom());
        TextView txtDoctorDegree = (TextView) findViewById(R.id.txt_doctor_degree);
        txtDoctorDegree.setText(mDoctorItem.getmDegree());
        TextView txtDoctorFee = (TextView) findViewById(R.id.txt_doctor_fee);
        txtDoctorFee.setText(mDoctorItem.getmFee());
        TextView txtDoctorHospital = (TextView) findViewById(R.id.txt_doctor_hospital);
        if (isExpert) {
            txtDoctorHospital.setVisibility(View.VISIBLE);
            txtDoctorHospital.setText(mDoctorItem.getmHospital());
        }
        mTxtOtherDoctors = (TextView) findViewById(R.id.txt_other_doctors);
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        String lang = pref.getString("lang", "zh");
        if (lang.equals("zh")) {
            mTxtOtherDoctors.setText(mDoctorItem.getmRoom() + " " + res.getString(R.string.other_doctors));
        }

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
        setDateFirstly();

        LinearLayout lytCurrentDate = (LinearLayout) findViewById(R.id.lyt_current_date);
        lytCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHlvMonthDate = (RecyclerView) findViewById(R.id.hlv_month_date);
        mHlvMonthDate.setLayoutManager(layoutManager);

        mMonthDateItems.add(new MonthDateItem(false, 1, 1, 1, 1, false, false));
        mMonthDateListAdapter = new MonthDateListAdapter(mMonthDateItems, mContext, ChooseAppointDoctorActivity.this);
        mHlvMonthDate.setAdapter(mMonthDateListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMoreNext(int page, int totalItemsCount, RecyclerView view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsMoreDate) {
                            int index = mMonthDateListAdapter.getItemCount() - 1;
                            loadMoreNextDate(index);
                        }
                    }
                });
            }
        };

        mHlvMonthDate.addOnScrollListener(scrollListener);

        mImgLeftDate = (ImageView) findViewById(R.id.img_left_date);
        mImgLeftDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                layoutManager.scrollToPositionWithOffset(firstVisibleItem+1, 0);
            }
        });
        mImgRightDate = (ImageView) findViewById(R.id.img_right_date);
        mImgRightDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem > 0) {
                    layoutManager.scrollToPositionWithOffset(firstVisibleItem - 1, 0);
                }
            }
        });

        mLstTimes = (ListView) findViewById(R.id.lst_times);
        mTimesItemListAdapter = new TimesItemListAdapter(mContext, ChooseAppointDoctorActivity.this);
        mLstTimes.setAdapter(mTimesItemListAdapter);
        mLstTimes.setOnTouchListener(new ListViewTouchListener());

        mImgUpTime = (ImageView) findViewById(R.id.img_up_time);
        mImgUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = mLstTimes.getFirstVisiblePosition();
                smoothScrollToPosition(mLstTimes, firstVisibleItem + 1);
            }
        });
        mImgDownTime = (ImageView) findViewById(R.id.img_down_time);
        mImgDownTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = mLstTimes.getFirstVisiblePosition();
                if (firstVisibleItem > 0) {
                    smoothScrollToPosition(mLstTimes, firstVisibleItem - 1);
                }
            }
        });

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHlvOtherDoctors = (RecyclerView) findViewById(R.id.hlv_other_doctors);
        mHlvOtherDoctors.setLayoutManager(layoutManager2);

        mOtherDoctorItems.add(new DoctorItem(0, 0, 0, " ", "", "", "", "", "0", "", 0, false));
        mOtherDoctorListAdapter = new OtherDoctorListAdapter(mOtherDoctorItems, mContext, ChooseAppointDoctorActivity.this);
        mHlvOtherDoctors.setAdapter(mOtherDoctorListAdapter);

        mImgLeftDoctor = (ImageView) findViewById(R.id.img_left_doctor);
        mImgLeftDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = layoutManager2.findFirstVisibleItemPosition();
                layoutManager2.scrollToPositionWithOffset(firstVisibleItem+1, 0);
            }
        });
        mImgRightDoctor = (ImageView) findViewById(R.id.img_right_doctor);
        mImgRightDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = layoutManager2.findFirstVisibleItemPosition();
                if (firstVisibleItem > 0) {
                    layoutManager2.scrollToPositionWithOffset(firstVisibleItem - 1, 0);
                }
            }
        });

        TextView btnSummary = (TextView) findViewById(R.id.btn_view_summary);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAppointDoctorActivity.this, SummaryViewActivity.class);
                intent.putExtra("Doctor", mDoctorItem);
                ChooseAppointDoctorActivity.this.startActivity(intent);
                ChooseAppointDoctorActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });

        TextView btnCallPhone = (TextView) findViewById(R.id.btn_call_phone);
        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDoctorItem.getmPhone() == null || mDoctorItem.getmPhone().equals("")) {
                    Toast.makeText(mContext, res.getString(R.string.no_phone), Toast.LENGTH_LONG).show();
                } else {
                    createActionSheet(mDoctorItem.getmPhone());
                }
            }
        });

        getWeekStatus();
    }

    @Override
    public void setAppoint(String time) {
        Intent intent;
        CommonUtils.appointYear = mYear;
        CommonUtils.appointMonth = mMonth;
        CommonUtils.appointDay = mDay;
        CommonUtils.appointWeekday = mWeekDay;
        CommonUtils.appointTime = time;
        CommonUtils.appointDoctorItem = mDoctorItem;
        if (CommonUtils.isLogin) {
            intent = new Intent(ChooseAppointDoctorActivity.this, AppointInfoViewActivity.class);
            if (mPatientID != null) {
                intent.putExtra("PatientID", mPatientID);
            }
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        } else {
            intent = new Intent(ChooseAppointDoctorActivity.this, LoginActivity.class);
            intent.putExtra("from_activity", "appoint");
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        }
    }

    @Override
    public void selectOtherDoctor(int position) {
        Intent intent = new Intent(ChooseAppointDoctorActivity.this, ChooseAppointDoctorActivity.class);
        DoctorItem doctorItem = mOtherDoctorListAdapter.getItem(position);
        intent.putExtra("isExpert", isExpert);
        intent.putExtra("Doctor", doctorItem);
        if (mPatientID != null) {
            intent.putExtra("PatientID", mPatientID);
        }
        this.startActivity(intent);
        this.finish();
        this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

}
