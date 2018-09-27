package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by naxing on 5/25/2018.
 */
public class NotificationItemView extends LinearLayout {

    Context mContext;
    private TextView mTitle;
    private TextView mTime;
    private TextView mDesc;
    private String type;
    private NotificationItem mItem;
    private LinearLayout backLayout;

    public NotificationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public NotificationItemView(Context context, NotificationItem item) {
        super(context);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.web_notification_item, this, true);

//        this.setClickable(true);

        mItem = item;
        type = item.getmType();

        mTitle = (TextView)findViewById(R.id.txt_title);
        if(type.equals("master")){
            mTitle.setVisibility(TextView.GONE);
        }
        else{
            mTitle.setText(item.getmTitle());
        }


        mTime = (TextView)findViewById(R.id.txt_time);
        mTime.setText(item.getmTime());

        mDesc = (TextView)findViewById(R.id.txt_content);
//        if(item.getmType().equals("master")){
            mDesc.setText(item.getmDesc());
            mDesc.setVisibility(TextView.VISIBLE);
//        }



//        backLayout = (LinearLayout)findViewById(R.id.ylt_noti_item);
//        backLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!type.equals("master")){
//                    Intent intent = new Intent(mContext, AboutViewActivity.class);
//                    intent.putExtra("uid", mItem.getmUid());
//                    intent.putExtra("hId", mItem.getmTID());
//                    intent.putExtra("isWebNotification", "1");
//                    intent.putExtra("title","网站通知");
//                    mContext.startActivity(intent);
//                }
//            }
//        });

    }

    public void setTime(String time) {
        mTime.setText(time);
    }
    public void setTitle(String name) {
        mTitle.setText(name);
    }
    public void setDesc(String desc) {
        mDesc.setText(desc);
    }
    public LinearLayout getLayout() {
        return backLayout;
    }
}
