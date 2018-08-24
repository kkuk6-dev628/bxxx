package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.EnterpriseItem;
import cn.reservation.app.baixingxinwen.utils.EnterpriseItemView;
import cn.reservation.app.baixingxinwen.utils.HistoryItem;
import cn.reservation.app.baixingxinwen.utils.HistoryItemView;

/**
 * Created by NSC on 6/6/2018.
 */
public class EnterpriseItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<EnterpriseItem> mItems = new ArrayList<EnterpriseItem>();

    public EnterpriseItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(EnterpriseItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<EnterpriseItem> lItems) {
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
        EnterpriseItemView itemView;
        if (convertView == null) {
            itemView = new EnterpriseItemView(mContext, mItems.get(position));
        }else{
            itemView = (EnterpriseItemView) convertView;

            itemView.setThumbnail(mItems.get(position).getmThumbnail());
            itemView.setName(mItems.get(position).getmName());
            itemView.setDesc(mItems.get(position).getmDesc());
            itemView.setPhone(mItems.get(position).getmPhone());
        }
        return itemView;
    }
}
