package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;

@SuppressWarnings("deprecation")
public class FamilyInfoViewActivity extends AppCompatActivity {

    public Resources res;
    private Context mContext;
    public AnimatedActivity pActivity;

    public FamilyMemberItem mFamilyItem;
    private int[] intGender = {R.string.male, R.string.female};
    TextView mTxtName;
    TextView mTxtGender;
    TextView mTxtBirthday;
    TextView mTxtCertification;
    TextView mTxtMobile;
    ImageView mImgUserIcon;
    private int[][] mIconResIDs = {{R.drawable.icon_man, R.drawable.icon_woman},
            {R.drawable.icon_grandfa, R.drawable.icon_grandma},
            {R.drawable.icon_boy, R.drawable.icon_girl}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info_view);

        Intent intent = getIntent();
        mFamilyItem = (FamilyMemberItem) intent.getSerializableExtra("FamilyItem");

        mContext = TabHostActivity.TabHostStack;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) FamilyInfoViewActivity.this.getParent();
        final TabActivity tabActivity = (TabActivity) FamilyInfoViewActivity.this.getParent().getParent();

        String pageTitle = res.getString(R.string.self_info);
        if (!mFamilyItem.getmRelation().equals("me")) {
            pageTitle = res.getString(R.string.family_info);
        }
        CommonUtils.customActionBar(mContext, this, true, pageTitle);

        mTxtName = (TextView) findViewById(R.id.txt_user_name);
        mTxtGender = (TextView) findViewById(R.id.txt_user_gender);
        mTxtBirthday = (TextView) findViewById(R.id.txt_user_birthday);
        mTxtCertification = (TextView) findViewById(R.id.txt_user_certification);
        mTxtMobile = (TextView) findViewById(R.id.txt_user_mobile);
        mImgUserIcon = (ImageView) findViewById(R.id.img_user_photo);

        loadData();

        TextView btnModifyInfo = (TextView) findViewById(R.id.btn_modify_info);
        btnModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(tabActivity, InputFamilyInfoActivity.class);
                intent.putExtra("FamilyItem", mFamilyItem);
                intent.putExtra("modify", true);
                tabActivity.startActivityForResult(intent, CommonUtils.REQUEST_CODE_MODIFY);
                tabActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });
    }

    public void loadData() {
        mTxtName.setText(mFamilyItem.getmMemberName());
        mTxtGender.setText(res.getString(intGender[mFamilyItem.getmMemberGender()]));
        mTxtCertification.setText(mFamilyItem.getmIdentify());
        mImgUserIcon.setImageResource(mIconResIDs[mFamilyItem.getmMemberType()][mFamilyItem.getmMemberGender()]);
        if (mFamilyItem.getmRelation().equals("me")) {
            mTxtBirthday.setText(CommonUtils.userInfo.getUserBirthday());
            mTxtMobile.setText(CommonUtils.userInfo.getUserPhone());
        } else {
            mTxtBirthday.setText("");
            mTxtMobile.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            pActivity.finishChildActivity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                //super.onBackPressed();
                pActivity.finishChildActivity();
            } else {
                getSupportFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.REQUEST_CODE_MODIFY && resultCode == CommonUtils.RESULT_CODE_MODIFY) {
            mFamilyItem = (FamilyMemberItem) data.getSerializableExtra("FamilyItem");
            loadData();

            Intent intent = new Intent();
            intent.setAction("cn.reservation.app.baixingxinwen.add_family");
            sendBroadcast(intent);
        }
    }
}
