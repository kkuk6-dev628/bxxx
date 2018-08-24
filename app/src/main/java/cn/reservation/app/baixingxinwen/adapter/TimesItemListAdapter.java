package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.ChooseAppointActivity;
import cn.reservation.app.baixingxinwen.utils.TimesItem;
import cn.reservation.app.baixingxinwen.utils.TimesItemView;

/**
 * Created by LiYin on 3/16/2017.
 */
public class TimesItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ChooseAppointActivity mParent;
    private ArrayList<TimesItem> mItems = new ArrayList<TimesItem>();

    public TimesItemListAdapter(Context context, ChooseAppointActivity parent) {
        mContext = context;
        mParent = parent;
    }

    public void addItem(TimesItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<TimesItem> lItems) {
        mItems = lItems;
    }

    public void setActive(int position) {
        for(int i=0; i<getCount(); i++) {
            if (i == position) {
                mItems.get(i).setmActive(true);
            } else {
                mItems.get(i).setmActive(false);
            }
        }
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
        TimesItemView itemView;
        final TimesItem item = mItems.get(position);
        if (convertView == null) {
            itemView = new TimesItemView(mContext, item);
        }else{
            itemView = (TimesItemView) convertView;

            itemView.setTxtTime(item.getmTime(), item.ismActive());
            itemView.setBtnAppoint(item.ismActive());
        }
        TextView btnAppoint = (TextView) itemView.findViewById(R.id.btn_appoint);
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.ismActive()){
                    mParent.setAppoint(item.getmTime());
                }
            }
        });
        return itemView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
