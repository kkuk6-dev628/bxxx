package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.RecommendDoctorItemView;

/**
 * Created by LiYin on 3/20/2017.
 */
public class RecommendDoctorItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DoctorItem> mItems = new ArrayList<DoctorItem>();

    public RecommendDoctorItemListAdapter(Context context) {
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
        RecommendDoctorItemView itemView;
        if (convertView == null) {
            itemView = new RecommendDoctorItemView(mContext);
        }else{
            itemView = (RecommendDoctorItemView) convertView;
        }
        itemView.setDoctorPhoto(mItems.get(position).getmPhoto());
        itemView.setDoctorName(mItems.get(position).getmName());
        itemView.setRoom(mItems.get(position).getmRoom());
        itemView.setHospital(mItems.get(position).getmHospital());
        itemView.setDoctorFee(mItems.get(position).getmFee());
        itemView.setDoctorDegree(mItems.get(position).getmDegree());
        return itemView;
    }
}
