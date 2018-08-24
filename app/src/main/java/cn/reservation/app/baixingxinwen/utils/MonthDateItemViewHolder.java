package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/15/2017.
 */
public class MonthDateItemViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private TextView mWeekday;
    private TextView mMonthDay;
    private TextView mMonthDayStatus;
    public LinearLayout mLayoutMonthDate;
    private String[] strWeekdays;

    public MonthDateItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        strWeekdays = mContext.getResources().getStringArray(R.array.weekday);

        mLayoutMonthDate = (LinearLayout) itemView.findViewById(R.id.lyt_month_date);
        mWeekday = (TextView) itemView.findViewById(R.id.txt_month_weekday);
        mMonthDay = (TextView) itemView.findViewById(R.id.txt_month_day);
        mMonthDayStatus = (TextView) itemView.findViewById(R.id.txt_month_day_status);
    }

    public void setWeekday(int weekday, boolean isRest, boolean isLast) {
        mWeekday.setText(strWeekdays[weekday]);
        if (isRest || isLast) {
            mWeekday.setTextColor(mContext.getResources().getColor(R.color.colorRestDate));
        } else {
            mWeekday.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
        }
    }

    public void setMonthDay(int monthDay, boolean isRest, boolean isLast, boolean isSelected) {
        mMonthDay.setText(Integer.toString(monthDay));
        if (isRest || isLast) {
            mMonthDay.setTextColor(mContext.getResources().getColor(R.color.colorRestDate));
            mMonthDay.setBackgroundResource(0);
        } else {
            if (!isSelected) {
                mMonthDay.setTextColor(mContext.getResources().getColor(R.color.colorSecondaryText));
                mMonthDay.setBackgroundResource(0);
            } else {
                mMonthDay.setTextColor(mContext.getResources().getColor(R.color.colorBackground));
                setMonthDatBackground(mContext, R.drawable.current_date_shape);
            }
        }
    }

    public void setMonthDayStatus(boolean isRest, boolean isLast) {
        if (isRest || isLast) {
            mMonthDayStatus.setText(mContext.getString(R.string.rest_date));
            mMonthDayStatus.setTextColor(mContext.getResources().getColor(R.color.colorRestStatus));
        } else {
            mMonthDayStatus.setText(mContext.getString(R.string.possible_date));
            mMonthDayStatus.setTextColor(mContext.getResources().getColor(R.color.colorPossibleStatus));
        }
    }

    private void setMonthDatBackground(Context context, int id) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mMonthDay.setBackgroundDrawable(ContextCompat.getDrawable(context, id));
        } else {
            mMonthDay.setBackground( ContextCompat.getDrawable(context, id));
        }
    }
}
