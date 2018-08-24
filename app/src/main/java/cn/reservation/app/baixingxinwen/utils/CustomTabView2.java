package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/20/2017.
 */
public class CustomTabView2 extends LinearLayout {

    private TextView mTxtTitle;
    private Resources res;

    public CustomTabView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabView2(Context context, String title, boolean selected) {
        super(context);
        res = context.getResources();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_tab2, this, true);

        mTxtTitle = (TextView) findViewById(R.id.txt_tab_title);
        mTxtTitle.setText(title);
        if (selected) {
            mTxtTitle.setTextColor(res.getColor(R.color.colorPrimaryText));
        } else {
            mTxtTitle.setTextColor(res.getColor(R.color.colorTabText));
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            mTxtTitle.setTextColor(res.getColor(R.color.colorPrimaryText));
        } else {
            mTxtTitle.setTextColor(res.getColor(R.color.colorTabText));
        }
    }

}
