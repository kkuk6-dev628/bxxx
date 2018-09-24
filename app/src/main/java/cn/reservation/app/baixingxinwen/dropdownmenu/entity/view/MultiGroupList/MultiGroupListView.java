package cn.reservation.app.baixingxinwen.dropdownmenu.entity.view.MultiGroupList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiiu.filter.interfaces.OnFilterDoneListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.reservation.app.baixingxinwen.R;

/**
 * auther: baiiu
 * time: 16/6/5 05 23:03
 * description:
 */
public class MultiGroupListView extends LinearLayout implements View.OnClickListener {

    private static final int TYPE_SELECT = 0;
    private static final int TYPE_INPUT = 1;

    private OnFilterDoneListener mOnFilterDoneListener;
    private JSONArray mGridData;
    private Context mContext;
    private LinearLayout mContentList;
    private int mColumnPosition;

    public MultiGroupListView(Context context) {
        super(context, null);
    }

    public MultiGroupListView(Context context, JSONArray gridData) {
        super(context, null);
        mGridData = gridData;
        init(context);
    }

    public MultiGroupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiGroupListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiGroupListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setBackgroundColor(Color.WHITE);
        inflate(context, R.layout.merge_filter_multi_group, this);
        ButterKnife.bind(this, this);

        mContentList = findViewById(R.id.lst_content);
        try {
            for (int i = 0; i < mGridData.length(); i++) {
                JSONObject item = mGridData.getJSONObject(i);
                MultiGroupItemViewBase itemView;
                if(getGroupItemType(i) == TYPE_INPUT){
                    itemView = new MultiGroupInputItemView(mContext);
                }
                else{
                    itemView = new MultiGroupSelectItemView(mContext);
                }
                itemView.initData(item);
                mContentList.addView(itemView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        MultiGroupListAdapter adapter = new MultiGroupListAdapter(mContext, mGridData);
//        mContentList.setAdapter(adapter);
    }

    public int getGroupItemType(int position) {
        try {
            JSONObject itemData = mGridData.getJSONObject(position);
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

    public MultiGroupListView setGridData(JSONArray gridData) {
        this.mGridData = gridData;
        return this;
    }

    public MultiGroupListView build() {

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 4);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override public int getSpanSize(int position) {
//                if (position == 0 || position == mTopGridData.size() + 1) {
//                    return 4;
//                }
//                return 1;
//            }
//        });
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setAdapter(new DoubleGridAdapter(getContext(), mTopGridData, mBottomGridList, this));

        return this;
    }

    private TextView mTopSelectedTextView;
    private TextView mBottomSelectedTextView;

    @Override
    public void onClick(View v) {

//        TextView textView = (TextView) v;
//        String text = (String) textView.getTag();

//        if (textView == mTopSelectedTextView) {
//            mTopSelectedTextView = null;
//            textView.setSelected(false);
//        } else if (textView == mBottomSelectedTextView) {
//            mBottomSelectedTextView = null;
//            textView.setSelected(false);
//        } else if (mTopGridData.contains(text)) {
//            if (mTopSelectedTextView != null) {
//                mTopSelectedTextView.setSelected(false);
//            }
//            mTopSelectedTextView = textView;
//            textView.setSelected(true);
//        } else {
//            if (mBottomSelectedTextView != null) {
//                mBottomSelectedTextView.setSelected(false);
//            }
//            mBottomSelectedTextView = textView;
//            textView.setSelected(true);
//        }
    }


    public MultiGroupListView setOnFilterDoneListener(OnFilterDoneListener listener) {
        mOnFilterDoneListener = listener;
        return this;
    }

    public HashMap<String, Object> getFilterParams(){
        HashMap<String, Object> ret = new HashMap<>();
        for(int i = 0 ; i < mContentList.getChildCount(); i++){
            MultiGroupItemViewBase itemView = (MultiGroupItemViewBase) mContentList.getChildAt(i);
            if(itemView != null){
                String filterValue = itemView.getFilterValue();
                String filterKey = itemView.getFilterKey();
                if(filterValue != null && !filterValue.isEmpty() && filterKey != null && !filterKey.isEmpty()){
                    ret.put(itemView.getFilterKey(), filterValue);
                }
            }
        }
        return ret;
    }

    public void setColumnPosition(int mColumnPosition) {
        this.mColumnPosition = mColumnPosition;
    }

    @OnClick(R.id.bt_confirm)
    public void clickDone() {

//        FilterUrl.instance().doubleGridTop = mTopSelectedTextView == null ? "" : (String) mTopSelectedTextView.getTag();
//        FilterUrl.instance().doubleGridBottom =
//                mBottomSelectedTextView == null ? "" : (String) mBottomSelectedTextView.getTag();

        if (mOnFilterDoneListener != null) {
            mOnFilterDoneListener.onFilterDone(mColumnPosition, "", "");
        }
    }

}
