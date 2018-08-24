package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.ChatItem;
import cn.reservation.app.baixingxinwen.utils.ChatItemView;

/**
 * Created by NanXing on 05/25/2018.
 */
public class ChatItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ChatItem> mItems = new ArrayList<ChatItem>();

    public ChatItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(ChatItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<ChatItem> lItems) {
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
        ChatItemView itemView;
        if (convertView == null) {
            itemView = new ChatItemView(mContext, mItems.get(position));
        }else {
            itemView = (ChatItemView) convertView;
        }

        itemView.setThumbnail(mItems.get(position).getmThumbnail());
        itemView.setDesc(mItems.get(position).getmDesc());
        itemView.setName(mItems.get(position).getmName());
        itemView.setTime(mItems.get(position).getmTime());
        return itemView;
    }
}
