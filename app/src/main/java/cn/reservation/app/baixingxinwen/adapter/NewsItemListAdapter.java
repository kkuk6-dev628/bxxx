package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.NewsItem;
import cn.reservation.app.baixingxinwen.utils.NewsItemView;

/**
 * Created by NSC on 6/3/2018.
 */
public class NewsItemListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewsItem> mItems = new ArrayList<NewsItem>();

    public NewsItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(NewsItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<NewsItem> lItems) {
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
        NewsItemView itemView;
        if (convertView == null) {
            itemView = new NewsItemView(mContext, mItems.get(position));
        }else{
            itemView = (NewsItemView) convertView;
            itemView.setThumbnail(mItems.get(position).getmImage());
            itemView.setTitle(mItems.get(position).getmTitle());
            itemView.setStartIime(mItems.get(position).getmStartTime());
            itemView.setEndTime(mItems.get(position).getmEndTime());
            itemView.setmTopTime(mItems.get(position).getmTopTime());
            itemView.setState(mItems.get(position).getmStatus());
            itemView.setPostState(mItems.get(position).getmPostState(),mItems.get(position).getmPaid());
            itemView.setViewCnt(mItems.get(position).getmViewCnt());
        }
        return itemView;
    }
}
