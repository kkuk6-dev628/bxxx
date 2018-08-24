package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.SearchItem;
import cn.reservation.app.baixingxinwen.utils.SearchItemView;

/**
 * Created by NanXing on 05/25/2018.
 */
public class SearchItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SearchItem> mItems = new ArrayList<SearchItem>();

    public SearchItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(SearchItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<SearchItem> lItems) {
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
        SearchItemView itemView;
        SearchItem itemData = mItems.get(position);
        if (convertView == null) {
            itemView = new SearchItemView(mContext, itemData);
        }else {
            itemView = (SearchItemView) convertView;
        }
        itemView.setDesc(itemData.getmDesc());
        itemView.setPrice(itemData.getmPrice());
        itemView.setProperty01("");
        itemView.setProperty02("");
        itemView.setProperty03("");
        itemView.setTitle01(itemData.getmTitle01());
        itemView.setTitle02(itemData.getmTitle02());
        itemView.setTitle03(itemData.getmTitle03());
        itemView.setPostState(itemData.getmPostState());
        itemView.setThumbnail(itemData.getmThumbnail(),itemData.getmFid(),itemData.getmSortid(),itemData.getmAdver());
        return itemView;
    }
}
