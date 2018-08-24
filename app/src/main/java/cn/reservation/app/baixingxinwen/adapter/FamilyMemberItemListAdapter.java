package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItemView;

/**
 * Created by LiYin on 3/17/2017.
 */
public class FamilyMemberItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FamilyMemberItem> mItems = new ArrayList<FamilyMemberItem>();

    public FamilyMemberItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(FamilyMemberItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<FamilyMemberItem> lItems) {
        mItems = lItems;
    }

    public void setChecked(int position) {
        for(int i=0; i<mItems.size(); i++) {
            if (i == position) {
                mItems.get(i).setSelected(true);
            } else {
                mItems.get(i).setSelected(false);
            }
        }
    }

    public FamilyMemberItem getCheckedItem() {
        for (int i=0; i<mItems.size(); i++) {
            if (mItems.get(i).isSelected()) {
                return mItems.get(i);
            }
        }
        return null;
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
        FamilyMemberItemView itemView;
        FamilyMemberItem item = mItems.get(position);
        if (convertView == null) {
            itemView = new FamilyMemberItemView(mContext);
        }else{
            itemView = (FamilyMemberItemView) convertView;
        }
        itemView.setMemberIcon(item.getmMemberType(), item.getmMemberGender());
        itemView.setMemberTitle(item.getmMemberType(), item.getmMemberGender());
        itemView.setCheckedIcon(item.isSelected());
        return itemView;
    }
}
