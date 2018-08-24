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
public class CustomTabView extends LinearLayout {

    private TextView mTxtTitle;
    private ImageView mImgIcon;
    private Resources res;

    public CustomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabView(Context context, String title, boolean selected, boolean first) {
        super(context);
        res = context.getResources();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_tab, this, true);

        mTxtTitle = (TextView) findViewById(R.id.txt_tab_title);
        mTxtTitle.setText(title);
        mImgIcon = (ImageView) findViewById(R.id.img_tab_icon);
        View mImgDivider = (View) findViewById(R.id.img_tab_divider);

        if (first) {
            if (selected) {
                mTxtTitle.setTextColor(res.getColor(R.color.colorPrimaryText));
                mImgIcon.setImageResource(R.drawable.navi_down_active);
            } else {
                mTxtTitle.setTextColor(res.getColor(R.color.colorTabText));
                mImgIcon.setImageResource(R.drawable.navi_down);
            }
        } else {
            if (selected) {
                mTxtTitle.setTextColor(res.getColor(R.color.colorPrimaryText));
            } else {
                mTxtTitle.setTextColor(res.getColor(R.color.colorTabText));
            }
        }
        if (first) mImgDivider.setVisibility(View.GONE);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            mTxtTitle.setTextColor(res.getColor(R.color.colorPrimaryText));
            mImgIcon.setImageResource(R.drawable.navi_down_active);
        } else {
            mTxtTitle.setTextColor(res.getColor(R.color.colorTabText));
            mImgIcon.setImageResource(R.drawable.navi_down);
        }
    }

}
