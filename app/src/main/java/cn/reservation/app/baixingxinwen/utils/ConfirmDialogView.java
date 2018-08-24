package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;

/**
 * Created by LiYin on 3/18/2017.
 */
public class ConfirmDialogView extends LinearLayout {

    public Context mContext;
    private TextView mTxtConfirmMsg;
    private ImageView mImgConfirmIcon;
    private int[] mResIds = {R.drawable.nave_done, R.drawable.navi_cancel};

    public ConfirmDialogView(Context context, int confirmType, String confirmMsg) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.confirm_dialog, this, true);

        mTxtConfirmMsg = (TextView)findViewById(R.id.txt_confirm_msg);
        mTxtConfirmMsg.setText(confirmMsg);

        mImgConfirmIcon = (ImageView) findViewById(R.id.img_confirm_icon);
        mImgConfirmIcon.setImageResource(mResIds[confirmType]);
    }

    public ConfirmDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
}
