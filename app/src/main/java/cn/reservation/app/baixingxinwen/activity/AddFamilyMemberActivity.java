package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.FamilyMemberItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;
import cz.msebera.android.httpclient.Header;

public class AddFamilyMemberActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    private TextView mBtnOK;

    private FamilyMemberItemListAdapter familyMemberItemListAdapter;
    private GridView mGridFamilyMembers;

    String userName;
    String userRelation;
    String userCertification;
    boolean isModify;
    int userMemberType;
    int userGender;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);

        Intent intent = getIntent();
        userName = intent.getStringExtra("UserName");
        userRelation = intent.getStringExtra("UserRelation");
        userCertification = intent.getStringExtra("UserCertification");
        isModify = intent.getBooleanExtra("modify", false);
        if (isModify) {
            userMemberType = intent.getIntExtra("UserMemberType", 0);
            userGender = intent.getIntExtra("UserGender", 0);
            userID = intent.getStringExtra("UserID");
        }

        mContext = AddFamilyMemberActivity.this;
        res = mContext.getResources();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.add_family));

        mGridFamilyMembers = (GridView) findViewById(R.id.grid_family_members);
        familyMemberItemListAdapter = new FamilyMemberItemListAdapter(mContext);
        mGridFamilyMembers.setAdapter(familyMemberItemListAdapter);
        mGridFamilyMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                familyMemberItemListAdapter.setChecked(i);
                mGridFamilyMembers.invalidateViews();
            }
        });
        for(int i=0; i<3; i++) {
            for(int j=0; j<2; j++) {
                boolean isChecked = false;
                if (isModify && userMemberType == i && userGender == j) isChecked = true;
                familyMemberItemListAdapter.addItem(new FamilyMemberItem("0", "", i, j, "", "", isChecked));
            }
        }
        familyMemberItemListAdapter.notifyDataSetChanged();

        mBtnOK = (TextView) findViewById(R.id.btn_ok);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFamily();
            }
        });
    }

    private void addFamily() {
        final FamilyMemberItem item = familyMemberItemListAdapter.getCheckedItem();
        if (item == null) {
            Toast.makeText(mContext, res.getString(R.string.please_check_icon), Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AddFamilyMemberActivity.this);
        RequestParams params = new RequestParams();
        params.put("userid", CommonUtils.userInfo.getUserID());
        params.put("token", CommonUtils.userInfo.getToken());
        params.put("name", userName);
        params.put("identify", userCertification);
        params.put("relation", userRelation);
        params.put("man_type", item.getmMemberType()+1);
        params.put("sex", item.getmMemberGender());

        String url = "register_family";
        if (isModify) {
            url = "update_family";
            params.put("id", userID);
        }
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    progressDialog.dismiss();
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_LONG).show();
                    } else if (response.getInt("code") == 102) {
                        Toast.makeText(mContext, res.getString(R.string.message_error_identify_inuse), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } else {
                        Intent resultIntent = new Intent();
                        int code = CommonUtils.RESULT_CODE_FAMILY;
                        if (isModify) {
                            code = CommonUtils.RESULT_CODE_MODIFY;
                            resultIntent.putExtra("MemberName", userName);
                            resultIntent.putExtra("Identify", userCertification);
                            resultIntent.putExtra("Relation", userRelation);
                            resultIntent.putExtra("MemberType", item.getmMemberType());
                            resultIntent.putExtra("Gender", item.getmMemberGender());
                        }
                        setResult(code, resultIntent);
                        AddFamilyMemberActivity.this.finish();
                        AddFamilyMemberActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
