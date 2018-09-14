package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.NotificationItem;
import cn.reservation.app.baixingxinwen.utils.NotificationItemView;

/**
 * Created by NanXing on 05/25/2018.
 */
public class NotificationItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NotificationItem> mItems = new ArrayList<NotificationItem>();

    public NotificationItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(NotificationItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<NotificationItem> lItems) {
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
        NotificationItemView itemView;
        if (convertView == null) {
            itemView = new NotificationItemView(mContext, mItems.get(position));
        }else {
            itemView = (NotificationItemView) convertView;
        }

        if(mItems.get(position).getmType().equals("master")){
            itemView.setDesc(mItems.get(position).getmDesc());
        }

        itemView.setTitle(mItems.get(position).getmTitle());
        itemView.setTime(mItems.get(position).getmTime());
        return itemView;
    }
}
