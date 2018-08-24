package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.AppointItemView;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;

/**
 * Created by LiYin on 3/19/2017.
 */
public class AppointItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AppointItem> mItems = new ArrayList<AppointItem>();

    public AppointItemListAdapter(Context context) {
        mContext = context;
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
        AppointItemView itemView;
        AppointItem item = mItems.get(position);
        if (convertView == null) {
            itemView = new AppointItemView(mContext);
        }else {
            itemView = (AppointItemView) convertView;
        }
        itemView.setDateTime(item.getmDate(), item.getmHour(), item.getmMinute());
        DoctorItem doctorItem = item.getmDoctor();
        itemView.setHospital(doctorItem.getmHospital(), doctorItem.getmName(), doctorItem.getmRoom());
        itemView.setPatient(item.getmPatient());

        return itemView;
    }
}
