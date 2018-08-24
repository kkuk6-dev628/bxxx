package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.ChooseAppointActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.MonthDateItem;
import cn.reservation.app.baixingxinwen.utils.MonthDateItemViewHolder;

/**
 * Created by LiYin on 3/15/2017.
 */
public class MonthDateListAdapter extends RecyclerView.Adapter<MonthDateItemViewHolder> {
    private Context mContext;
    private List<MonthDateItem> mItems = Collections.emptyList();
    private ChooseAppointActivity mParent;

    public MonthDateListAdapter(List<MonthDateItem> aItems, Context context, ChooseAppointActivity parent) {
        this.mContext = context;
        this.mItems = aItems;
        this.mParent = parent;
    }

    public void addItem(MonthDateItem aItem) {
        mItems.add(aItem);
    }

    public void insertItem(MonthDateItem aItem) {
        mItems.add(0, aItem);
    }

    public void setListItems(ArrayList<MonthDateItem> lItems) {
        mItems = lItems;
    }

    public MonthDateItem getItem(int position) {
        return mItems.get(position);
    }

    public void setClear() {
        mItems.clear();
    }

    @Override
    public MonthDateItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.month_date_item, parent, false);
        itemView.setLayoutParams(CommonUtils.getLayoutParams(mParent, 7, CommonUtils.getPixelValue(mContext, 50)));
        return new MonthDateItemViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MonthDateItemViewHolder holder, final int position) {
        MonthDateItem item = mItems.get(position);
        holder.setWeekday(item.getmWeekday(), item.ismRest(), item.getmLast());
        holder.setMonthDay(item.getmDay(), item.ismRest(), item.getmLast(), item.ismSelected());
        holder.setMonthDayStatus(item.ismRest(), item.getmLast());

        holder.mLayoutMonthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItems.get(position).ismRest() && !mItems.get(position).getmLast()) {
                    mItems.get(mParent.mCurIndex).setmSelected(false);
                    mParent.mDay = mItems.get(position).getmDay();
                    mParent.mMonth = mItems.get(position).getmMonth();
                    mParent.mYear = mItems.get(position).getmYear();
                    mItems.get(position).setmSelected(true);
                    mParent.mCurIndex = position;
                    mParent.setCurrentDate();
                    mParent.loadTimesList();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
