package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.ReAppointActivity;
import cn.reservation.app.baixingxinwen.utils.AppointHistoryItemView;
import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;

/**
 * Created by LiYin on 3/20/2017.
 */
public class AppointHistoryItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AppointItem> mItems = new ArrayList<AppointItem>();
    private ReAppointActivity mParent;

    public AppointHistoryItemListAdapter(Context context, ReAppointActivity parent) {
        mContext = context;
        mParent = parent;
    }

    public void addItem(AppointItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<AppointItem> lItems) {
        mItems = lItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AppointHistoryItemView itemView;
        final AppointItem item = mItems.get(position);
        if (convertView == null) {
            itemView = new AppointHistoryItemView(mContext);
        }else {
            itemView = (AppointHistoryItemView) convertView;
        }
        itemView.setDate(item.getmDate(), item.getmHour(), item.getmMinute());
        DoctorItem doctorItem = item.getmDoctor();
        itemView.setImgDoctorPhoto(doctorItem.getmPhoto());
        itemView.setHospital(doctorItem.getmHospital());
        itemView.setDoctorName(doctorItem.getmName());
        itemView.setRoom(doctorItem.getmRoom());
        itemView.setPatient(item.getmPatient());

        TextView btnAppoint = (TextView) itemView.findViewById(R.id.btn_appoint);
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParent.setAppoint(item);
            }
        });

        return itemView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
