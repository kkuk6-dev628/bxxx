package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.HospitalItem;
import cn.reservation.app.baixingxinwen.utils.HospitalItemView;

/**
 * Created by LiYin on 3/13/2017.
 */
public class HospitalItemListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HospitalItem> mItems = new ArrayList<HospitalItem>();

    public HospitalItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(HospitalItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<HospitalItem> lItems) {
        mItems = lItems;
    }

    public void clearItems(){
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
        HospitalItemView itemView;
        if (convertView == null) {
            itemView = new HospitalItemView(mContext, mItems.get(position));
        }else{
            itemView = (HospitalItemView) convertView;

            itemView.setThumbnail(mItems.get(position).getmThumbnail());
            itemView.setTitle(mItems.get(position).getmTitle());
            itemView.setDesc(mItems.get(position).getmDesc());
            itemView.setRegion(mItems.get(position).getmRegion());
        }
        return itemView;
    }
}
