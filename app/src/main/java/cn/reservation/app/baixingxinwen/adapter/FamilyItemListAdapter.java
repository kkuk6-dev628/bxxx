package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.FamilyItemView;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;

/**
 * Created by LiYin on 3/17/2017.
 */
public class FamilyItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FamilyMemberItem> mItems = new ArrayList<FamilyMemberItem>();

    public FamilyItemListAdapter(Context context) {
        mContext = context;
    }

    public void addItem(FamilyMemberItem aItem) {
        mItems.add(aItem);
    }

    public void setListItems(ArrayList<FamilyMemberItem> lItems) {
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
        FamilyItemView itemView;
        FamilyMemberItem aItem = mItems.get(position);
        if (convertView == null) {
            itemView = new FamilyItemView(mContext, aItem);
        }else{
            itemView = (FamilyItemView) convertView;
        }
        itemView.setFamilyIcon(aItem.getmMemberType(), aItem.getmMemberGender());

        if (aItem.getmRelation().equals("me")) {
            itemView.setName(mContext.getResources().getString(R.string.self), aItem.getmMemberID());
        } else {
            itemView.setName(aItem.getmMemberName(), aItem.getmMemberID());
        }
        return itemView;
    }

}
