package cn.reservation.app.baixingxinwen.dropdownmenu.entity.view.MultiGroupList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by naxing on 5/25/2018.
 */
public class MultiGroupInputItemView extends MultiGroupItemViewBase {

    private EditText mMinValue;
    private EditText mMaxValue;

    public MultiGroupInputItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MultiGroupInputItemView(Context context) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.merge_filter_multi_group_item_input, this, true);
        mTitleView = findViewById(R.id.txt_title);
        mMinValue = findViewById(R.id.edit_min_price);
        mMaxValue = findViewById(R.id.edit_max_price);
    }

    @Override
    public void initData(final JSONObject item){
        rootItem = item;
            try {
                String unit = rootItem.optString("unit");
                mMinValue.setHint("最低值（" + unit + "）");
                mMinValue.setHint("最高值（" + unit + "）");

                mTitleView.setText(rootItem.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public String getFilterKey() {
        return super.getFilterKey();
    }

    @Override
    public String getFilterValue() {
        String minValue = mMinValue.getText().toString();
        String maxValue = mMaxValue.getText().toString();
        if(minValue.isEmpty() || maxValue.isEmpty()){
            return "clear";
        }
        return minValue + "<->" + maxValue;
    }
}
