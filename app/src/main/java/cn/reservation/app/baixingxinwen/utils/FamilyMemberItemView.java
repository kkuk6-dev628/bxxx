package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/17/2017.
 */
public class FamilyMemberItemView extends LinearLayout {
    private Context mContext;
    private ImageView mImgMemberIcon;
    private TextView mTxtMemberTitle;
    private ImageView mImgCheckedIcon;
    private int[][] mIconResIDs = {{R.drawable.icon_man, R.drawable.icon_woman},
            {R.drawable.icon_grandfa, R.drawable.icon_grandma},
            {R.drawable.icon_boy, R.drawable.icon_girl}, {R.drawable.icon_add_user}};
    String[] strMembers;
    String[] strGenders;

    public FamilyMemberItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public FamilyMemberItemView(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.family_member_item, this, true);


        mTxtMemberTitle = (TextView) findViewById(R.id.txt_member_title);
        mImgMemberIcon = (ImageView) findViewById(R.id.img_member_icon);
        mImgCheckedIcon = (ImageView) findViewById(R.id.img_checked_icon);

        strMembers = context.getResources().getStringArray(R.array.members);
        strGenders = context.getResources().getStringArray(R.array.gender);
    }

    public void setMemberIcon(int memberType, int gender) {
        mImgMemberIcon.setImageResource(mIconResIDs[memberType][gender]);
    }

    public void setMemberTitle(int memberType, int gender) {
        mTxtMemberTitle.setText(strMembers[memberType] + "(" + strGenders[gender] + ")");
    }

    public void setMemberName(String name) {
        mTxtMemberTitle.setText(name);
    }

    public void setCheckedIcon(boolean selected) {
        if (selected) {
            mImgCheckedIcon.setVisibility(View.VISIBLE);
        } else {
            mImgCheckedIcon.setVisibility(View.GONE);
        }
    }
}
