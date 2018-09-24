package cn.reservation.app.baixingxinwen.dropdownmenu.entity.view.MultiGroupList;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by naxing on 5/25/2018.
 */
public class MultiGroupSelectItemView extends MultiGroupItemViewBase implements View.OnClickListener{

    private JSONObject mChoice;

    private FlexboxLayout mContentView;

    private View mSelected;

    private boolean mIsClear;

    public MultiGroupSelectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MultiGroupSelectItemView(Context context) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.merge_filter_multi_group_item_grid, this, true);
        mTitleView = findViewById(R.id.txt_title);
        mContentView = findViewById(R.id.lyt_content);
    }

    @Override
    public void initData(final JSONObject item){
        rootItem = item;
        if(item.has("choice")){
            try {
                mTitleView.setText(rootItem.getString("title"));
                mChoice = rootItem.getJSONObject("choice");
                mContentView.removeAllViews();
                Iterator<String> iter = mChoice.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        String value = mChoice.getString(key);
                        TextView button = new TextView(mContext);
                        button.setTag(key);
                        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        lp.setMargins(15,15,15,15);
                        button.setLayoutParams(lp);
                        button.setPadding(15,15,15,15);
                        button.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_small_white));
                        button.setText(value);
                        button.setClickable(true);
                        button.setTextSize(15);
                        button.setOnClickListener(this);
                        mContentView.addView(button);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getFilterValue() {
        if(mSelected != null){
            if(mIsClear){
                return "clear";
            }
            String typeInfoItem = rootItem.optString("type");
            String analysisInfoItem = rootItem.optString("analysis");

            String selectedString = (String)mSelected.getTag();
            if(analysisInfoItem.contains("sortid") || typeInfoItem.contains("click")){
                int pos = Integer.parseInt(selectedString);
                String sortid = rootItem.optString("optionid");
                String[] sortids = sortid.split(",");
                if(sortids.length > 1 && pos > 0 && pos <=2){
                    return sortids[pos-1];
                }
                else{
                    return null;
                }
            }

            return selectedString;
        }
        return "clear";
    }

    @Override
    public String getFilterKey() {
        return super.getFilterKey();
    }

    @Override
    public void onClick(View view) {
        setSelectedColor((TextView)view);
        clearSelectedColor((TextView) mSelected);
        if(mSelected == view && !mIsClear)
        {
            mIsClear = true;
        }
        else{
            mSelected = view;
            mIsClear = false;
        }
    }

    private void setSelectedColor(TextView view){
        if(view == null){
            return;
        }

        view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_small_selected));
        view.setTextColor(getResources().getColor(R.color.colorConfirmText));
    }

    private void clearSelectedColor(TextView view){
        if(view == null){
            return;
        }
        view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_small_white));
        view.setTextColor(getResources().getColor(R.color.black_p50));
    }

}
