package cn.reservation.app.baixingxinwen.dropdownmenu.entity.view.MultiGroupList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NanXing on 05/25/2018.
 */
public class MultiGroupListAdapter extends BaseAdapter {

    private static final int TYPE_SELECT = 0;
    private static final int TYPE_INPUT = 1;
    private static final int TYPE_MAX_COUNT = TYPE_INPUT + 1;

    private Context mContext;
    private JSONArray mItems = new JSONArray();

    public MultiGroupListAdapter(Context context) {
        mContext = context;
    }

    public MultiGroupListAdapter(Context context, JSONArray items) {
        mContext = context;
        mItems = items;
    }

//    public void addItem(SearchItem aItem) {
//        mItems.add(aItem);
//    }

    public void setListItems(JSONArray lItems) {
        mItems = lItems;
    }

//    public void clearItems(){
//        mItems.;
//    }

    @Override
    public int getItemViewType(int position) {
        try {
            JSONObject itemData = mItems.getJSONObject(position);
            String type = itemData.getString("type");
            if(type.equals("number") || type.equals("price")){
                return TYPE_INPUT;
            }
            return TYPE_SELECT;
        } catch (JSONException e) {
            e.printStackTrace();
            return TYPE_SELECT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mItems.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return mItems.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        JSONObject itemData = null;
        try {
            itemData = mItems.getJSONObject(position);

            if(getItemViewType(position) == TYPE_INPUT){
                MultiGroupInputItemView itemView = null;
                itemView = new MultiGroupInputItemView(mContext);
//                if (convertView == null) {
//                    itemView = new MultiGroupInputItemView(mContext);
//                }else {
//                    itemView = (MultiGroupInputItemView) convertView;
//                }
                itemView.initData(itemData);
                return itemView;
            }
            else{
                MultiGroupSelectItemView itemView = null;
                itemView = new MultiGroupSelectItemView(mContext);
//                if (convertView == null) {
//                    itemView = new MultiGroupSelectItemView(mContext);
//                }else {
//                    itemView = (MultiGroupSelectItemView) convertView;
//                }
                itemView.initData(itemData);
                return itemView;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
