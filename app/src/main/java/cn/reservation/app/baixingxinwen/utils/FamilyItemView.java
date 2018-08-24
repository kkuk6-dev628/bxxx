package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/17/2017.
 */
public class FamilyItemView extends LinearLayout {
    private Context mContext;
    private ImageView mImgFamilyIcon;
    private TextView mTxtFamilyName;
    private int[][] mIconResIDs = {{R.drawable.icon_man, R.drawable.icon_woman},
            {R.drawable.icon_grandfa, R.drawable.icon_grandma},
            {R.drawable.icon_boy, R.drawable.icon_girl}, {R.drawable.icon_add_user}};

    public FamilyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public FamilyItemView(Context context, FamilyMemberItem aItems) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.family_item, this, true);

        mImgFamilyIcon = (ImageView) findViewById(R.id.img_family_icon);
        mTxtFamilyName = (TextView) findViewById(R.id.txt_family_name);
    }

    public void setFamilyIcon(int userType, int userGender) {
        mImgFamilyIcon.setImageResource(mIconResIDs[userType][userGender]);
    }

    public void setName(String name, String id) {
        mTxtFamilyName.setText(name);
        if (id.equals("0")) {
            mTxtFamilyName.setTextColor(getResources().getColor(R.color.colorTabText));
        } else {
            mTxtFamilyName.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }
}
