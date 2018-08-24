package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/18/2017.
 */
public class PatientItemViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;;
    private TextView mTxtMemberName;
    private ImageView mImgMemberIcon;
    public RelativeLayout mLayoutMembers;
    private ImageView mImgCheckedIcon;
    private int[][] mIconResIDs = {{R.drawable.icon_man, R.drawable.icon_woman},
            {R.drawable.icon_grandfa, R.drawable.icon_grandma},
            {R.drawable.icon_boy, R.drawable.icon_girl}, {R.drawable.icon_add_user}};

    public PatientItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mLayoutMembers = (RelativeLayout) itemView.findViewById(R.id.lyt_member_item);
        mTxtMemberName = (TextView) itemView.findViewById(R.id.txt_member_title);
        mImgMemberIcon = (ImageView) itemView.findViewById(R.id.img_member_icon);
        mImgCheckedIcon = (ImageView) itemView.findViewById(R.id.img_checked_icon);
    }

    public void setMemberIcon(int memberType, int gender) {
        mImgMemberIcon.setImageResource(mIconResIDs[memberType][gender]);
    }

    public void setMemberName(String name) {
        mTxtMemberName.setText(name);
    }

    public void setCheckedIcon(boolean selected) {
        if (selected) {
            mImgCheckedIcon.setVisibility(View.VISIBLE);
        } else {
            mImgCheckedIcon.setVisibility(View.GONE);
        }
    }
}
