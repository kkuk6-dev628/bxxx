package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;

public class InputFamilyInfoActivity extends AppCompatActivity {

    private Context mContext;
    private Resources res;
    private EditText mEditUserName;
    private EditText mEditUserRelation;
    private EditText mEditUserCertification;

    private FamilyMemberItem mFamilyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_family_info);

        mContext = InputFamilyInfoActivity.this;
        res = mContext.getResources();

        Intent intent = getIntent();
        mFamilyItem = (FamilyMemberItem) intent.getSerializableExtra("FamilyItem");

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.input_family_info));

        mEditUserName = (EditText) findViewById(R.id.edit_user_name);
        mEditUserRelation = (EditText) findViewById(R.id.edit_user_relation);
        mEditUserCertification = (EditText) findViewById(R.id.edit_user_certification);

        if (mFamilyItem != null) {
            mEditUserName.setText(mFamilyItem.getmMemberName());
            if (mFamilyItem.getmRelation().equals("me")) {
                mEditUserRelation.setText(res.getString(R.string.self));
                mEditUserRelation.setEnabled(false);
            } else {
                mEditUserRelation.setText(mFamilyItem.getmRelation());
                mEditUserRelation.setEnabled(true);
            }
            mEditUserCertification.setText(mFamilyItem.getmIdentify());
        }

        mEditUserCertification.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    gotoForward();
                    return true;
                }
                return false;
            }
        });

        TextView btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoForward();
            }
        });
    }

    public void gotoForward() {
        if (!validateInput()) {
            return;
        }
        Intent intent = new Intent(InputFamilyInfoActivity.this, AddFamilyMemberActivity.class);
        intent.putExtra("UserName", mEditUserName.getText().toString());
        intent.putExtra("UserRelation", mEditUserRelation.getText().toString());
        intent.putExtra("UserCertification", mEditUserCertification.getText().toString());
        if (mFamilyItem != null) {
            intent.putExtra("UserMemberType", mFamilyItem.getmMemberType());
            intent.putExtra("UserGender", mFamilyItem.getmMemberGender());
            intent.putExtra("UserID", mFamilyItem.getmMemberID());
            intent.putExtra("modify", true);
            if (mFamilyItem.getmRelation().equals("me")) {
                intent.putExtra("UserRelation", "me");
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        int code = CommonUtils.REQUEST_CODE_FAMILY;
        if (mFamilyItem != null) code = CommonUtils.REQUEST_CODE_MODIFY;
        InputFamilyInfoActivity.this.startActivityForResult(intent, code);
        InputFamilyInfoActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    private boolean validateInput() {
        if (mEditUserName.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_name), Toast.LENGTH_LONG).show();
            return false;
        }
        if (mEditUserRelation.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_relation), Toast.LENGTH_LONG).show();
            return false;
        }
        if (mEditUserCertification.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_certification), Toast.LENGTH_LONG).show();
            return false;
        } else if (mEditUserCertification.getText().toString().length() != 18) {
            Toast.makeText(mContext, res.getString(R.string.message_error_identify_format), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.REQUEST_CODE_FAMILY && resultCode == CommonUtils.RESULT_CODE_FAMILY) {
            setResult(CommonUtils.RESULT_CODE_FAMILY);

            Intent intent = new Intent();
            intent.setAction("cn.reservation.app.baixingxinwen.add_family");
            sendBroadcast(intent);

            finish();
            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        } else if (requestCode == CommonUtils.REQUEST_CODE_MODIFY && resultCode == CommonUtils.RESULT_CODE_MODIFY) {
            mFamilyItem.setmMemberName(data.getStringExtra("MemberName"));
            mFamilyItem.setmIdentify(data.getStringExtra("Identify"));
            mFamilyItem.setmRelation(data.getStringExtra("Relation"));
            mFamilyItem.setmMemberType(data.getIntExtra("MemberType", mFamilyItem.getmMemberType()));
            mFamilyItem.setmMemberGender(data.getIntExtra("Gender", mFamilyItem.getmMemberGender()));
            Intent resultIntent = new Intent();
            resultIntent.putExtra("FamilyItem", mFamilyItem);
            setResult(CommonUtils.RESULT_CODE_MODIFY, resultIntent);
            finish();
            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        }
    }
}
