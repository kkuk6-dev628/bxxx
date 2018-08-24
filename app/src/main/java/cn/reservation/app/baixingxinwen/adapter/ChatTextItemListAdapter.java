package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.ChatTextItem;
import cn.reservation.app.baixingxinwen.utils.ChatTextItemView;

/**
 * Created by NanXing on 05/25/2018.
 */
public class ChatTextItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ChatTextItem> mItems = new ArrayList<ChatTextItem>();

    public ChatTextItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(ChatTextItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<ChatTextItem> lItems) {
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
        ChatTextItemView itemView;
        if (convertView == null) {
            itemView = new ChatTextItemView(mContext, mItems.get(position));
        }else {
            itemView = (ChatTextItemView) convertView;
        }

        itemView.setThumbnail(mItems.get(position).getmThumbnail());
        itemView.setmeThumbnail(mItems.get(position).getmeThumbnail());
        itemView.setDesc(mItems.get(position).getmDesc());
        itemView.setmeName(mItems.get(position).getmeName());
        itemView.setmeDesc(mItems.get(position).getmeDesc());
        itemView.setName(mItems.get(position).getmName());
        itemView.setTime(mItems.get(position).getmTime());
        return itemView;
    }
}
