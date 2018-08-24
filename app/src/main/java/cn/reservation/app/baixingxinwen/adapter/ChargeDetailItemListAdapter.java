package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.ChargeDetailItem;
import cn.reservation.app.baixingxinwen.utils.ChargeDetailtemView;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItemView;

/**
 * Created by LiYin on 3/20/2017.
 */
public class ChargeDetailItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ChargeDetailItem> mItems = new ArrayList<ChargeDetailItem>();

    public ChargeDetailItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(ChargeDetailItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<ChargeDetailItem> lItems) {
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
        ChargeDetailtemView itemView;
        if (convertView == null) {
            itemView = new ChargeDetailtemView(mContext);
        }else {
            itemView = (ChargeDetailtemView) convertView;
        }

        itemView.setDate(mItems.get(position).getmDate());
        itemView.setValue(mItems.get(position).getmValue());
        itemView.setContent(mItems.get(position).getmContent());
        return itemView;
    }
}
