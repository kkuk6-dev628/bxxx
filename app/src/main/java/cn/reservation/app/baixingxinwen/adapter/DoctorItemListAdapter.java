package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.DoctorItemView;

/**
 * Created by LiYin on 3/15/2017.
 */
public class DoctorItemListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DoctorItem> mItems = new ArrayList<DoctorItem>();

    public DoctorItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(DoctorItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<DoctorItem> lItems) {
        mItems = lItems;
    }

    public void clearItems() {
        mItems.clear();
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
        DoctorItemView itemView;
        DoctorItem item = mItems.get(position);
        if (convertView == null) {
            itemView = new DoctorItemView(mContext, item);
        }else{
            itemView = (DoctorItemView) convertView;

            itemView.setPhoto(item.getmPhoto());
            itemView.setDoctorName(item.getmName());
            itemView.setHospital(item.getmHospital(), item.isDoctor());
            itemView.setRoom(item.getmRoom(), item.isDoctor());
            itemView.setDegree(item.getmDegree(), item.isDoctor());
            itemView.setFee(item.getmFee());
        }
        return itemView;
    }
}
