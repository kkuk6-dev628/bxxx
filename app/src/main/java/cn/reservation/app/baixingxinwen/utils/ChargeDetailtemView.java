package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/20/2017.
 */
public class ChargeDetailtemView extends LinearLayout{
    private Context mContext;
    private TextView mTxtDate;
    private TextView mTxtContent;
    private TextView mTxtValue;

    public ChargeDetailtemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
    public ChargeDetailtemView(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.charge_item, this, true);

        mTxtDate = (TextView) findViewById(R.id.txt_charge_detail_date);
        mTxtContent = (TextView) findViewById(R.id.txt_charge_detail_desc);
        mTxtValue = (TextView) findViewById(R.id.txt_charge_detail_value);
    }

    public void setDate(String date) { mTxtDate.setText(date); }
    public void setContent(String content) {
        mTxtContent.setText(content);
    }
    public void setValue(String value){
        mTxtValue.setText(value);
        if(Float.valueOf(value)>0f){
            mTxtValue.setTextColor(getResources().getColor(R.color.colorConfirmText));
        }else{
            mTxtValue.setTextColor(getResources().getColor(R.color.colorComment));
        }
    }

}
