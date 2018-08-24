package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.HistoryItem;
import cn.reservation.app.baixingxinwen.utils.HistoryItemView;

/**
 * Created by LiYin on 3/13/2017.
 */
public class HistoryItemListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HistoryItem> mItems = new ArrayList<HistoryItem>();

    public HistoryItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(HistoryItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<HistoryItem> lItems) {
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        HistoryItemView itemView;
        RecyclerView.ViewHolder holder = null;
        if (convertView == null) {
            itemView = new HistoryItemView(mContext, mItems.get(position));
        }else {
            itemView = (HistoryItemView) convertView;
        }
        itemView.setThumbnail(mItems.get(position).getmThumbnail());
        itemView.setDate(mItems.get(position).getmDate());
        itemView.setDesc(mItems.get(position).getmDesc());
        itemView.setPrice(mItems.get(position).getmPrice());
        itemView.setChk(mItems.get(position).getmChk());
        itemView.setChkPanel(mItems.get(position).getmChkPanel());
        itemView.setPostState(mItems.get(position).getmPostState());

        itemView.getChk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("1"+mItems.get(position).getmChk());
                if(mItems.get(position).getmChk()){
                    mItems.get(position).setmChk(false);
                }else{
                    mItems.get(position).setmChk(true);
                }
                System.out.println("2"+mItems.get(position).getmChk());
                //historyItem.setmChk(mChk.isChecked());
                //mChk.setChecked(historyItem.getmChk());
            }
        });

        LinearLayout lyt_room_favor_thumbnail =(LinearLayout)itemView.findViewById(R.id.lyt_room_favor_thumbnail);
        lyt_room_favor_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("id"+ mItems.get(position).getmHistoryID());

            }
        });
        return itemView;
    }
}
