package cn.reservation.app.baixingxinwen.dropdownmenu.entity.view.MultiGroupList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naxing on 5/25/2018.
 */
public class MultiGroupItemViewBase extends LinearLayout {

    protected Context mContext;
    protected JSONObject rootItem;

    protected TextView mTitleView;

    public MultiGroupItemViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MultiGroupItemViewBase(Context context) {
        super(context);

        mContext = context;
    }

    public String getFilterValue(){
        return null;
    }

    public String getFilterKey(){
        try {
            if(rootItem != null){
                String typeInfoItem = rootItem.optString("type");
                String analysisInfoItem = rootItem.optString("analysis");

                if(analysisInfoItem.contains("sortid") || typeInfoItem.contains("click")){
                    return "sortid";
                }

                String optionId = rootItem.getString("optionid");
                return "option_" + optionId;
            }
            else{
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initData(final JSONObject item){
    }
}
