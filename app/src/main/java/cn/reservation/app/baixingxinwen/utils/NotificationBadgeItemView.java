package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by naxing on 5/25/2018.
 */
public class NotificationBadgeItemView extends FrameLayout {

    Context mContext;
    private TextView mBadge;

    public NotificationBadgeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public NotificationBadgeItemView(Context context) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.badge, this, true);
    }

}
