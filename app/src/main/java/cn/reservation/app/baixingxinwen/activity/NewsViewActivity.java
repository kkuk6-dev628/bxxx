package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cz.msebera.android.httpclient.Header;
@SuppressWarnings("deprecation")
public class NewsViewActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener {

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    public ImageView mImgArticle;
    public TextView mTxtTitle;
    public TextView mTxtContent;
    public TextView mTxtAddressHospital;
    public TextView mTxtAddressStreet;
    public TextView mTxtViewMap;
    public TextView mTxtCallPhone;

    public long mHospitalID;
    public String mHospitalTel;
    public String mHospitalName;
    public String mCityName;

    public HealthArticleItem healthArticleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) NewsViewActivity.this.getParent();
        final TabActivity tabActivity = (TabActivity) NewsViewActivity.this.getParent().getParent();


        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.news));

        Intent intent = getIntent();
        healthArticleItem = (HealthArticleItem) intent.getSerializableExtra("Article");

        mImgArticle = (ImageView) findViewById(R.id.img_article);
        mTxtTitle = (TextView) findViewById(R.id.txt_article_title);
        mTxtContent = (TextView) findViewById(R.id.txt_article_content);
        mTxtViewMap = (TextView) findViewById(R.id.txt_view_map);
        mTxtCallPhone = (TextView) findViewById(R.id.txt_call_phone);
        mTxtAddressHospital = (TextView) findViewById(R.id.txt_address_hospital);
        mTxtAddressStreet = (TextView) findViewById(R.id.txt_address_street);

        Picasso
                .with(mContext)
                .load(healthArticleItem.getmImage())
                .into(mImgArticle);
        mTxtTitle.setText(healthArticleItem.getmTitle());
        //mTxtContent.setText(healthArticleItem.getmContent());

        TextView txtAppoint = (TextView) findViewById(R.id.txt_appoint);
        txtAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mTxtCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHospitalTel == null || mHospitalTel.equals("")) {
                    Toast.makeText(mContext, res.getString(R.string.no_phone), Toast.LENGTH_LONG).show();
                } else {
                    createActionSheet(mHospitalTel);
                }
            }
        });
        getArticle();

        mTxtViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tabActivity, BaiduMapActivity.class);
                intent.putExtra("HospitalID", mHospitalID);
                intent.putExtra("hospital", mHospitalName);
                intent.putExtra("city", mCityName);
                tabActivity.startActivity(intent);
                tabActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });
    }

    private void getArticle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, NewsViewActivity.this);
                RequestParams params = new RequestParams();
                params.put("id", healthArticleItem.getmArticleID());
                params.put("lang", CommonUtils.mIntLang);
                if (CommonUtils.isLogin) {
                    params.put("userid", CommonUtils.userInfo.getUserID());
                    params.put("token", CommonUtils.userInfo.getToken());
                }

                String url = "get_news_article_content";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                JSONObject article = response.getJSONObject("article");
                                mTxtContent.setText(article.getString("contents"));
                                mHospitalID = article.getLong("WHospNo");
                                mHospitalTel = article.getString("HospTel");
                                mHospitalName = article.getString("HospName");
                                //mCityName = article.getString("City");
                                mCityName = "图们";
                                mTxtAddressHospital.setText(article.getString("HospName"));
                                mTxtAddressStreet.setText(article.getString("Address"));
                                if (CommonUtils.isLogin) {
                                    Intent intent = new Intent();
                                    intent.setAction("cn.reservation.app.baixingxinwen.update_news");
                                    sendBroadcast(intent);
                                }
                            }
                            CommonUtils.dismissProgress(progressDialog);

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
        }, 500);
    }

    public void createActionSheet(String phones){
        String[] phone = phones.split(",");
        ActionSheet.createBuilder(mContext, NewsViewActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle(res.getString(R.string.str_cancel))
                .setOtherButtonTitles(phone)
                .setCancelableOnTouchOutside(true)
                .setListener(NewsViewActivity.this)
                .show();
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
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        String[] phone = mHospitalTel.split(",");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone[index]));
        startActivity(intent);
    }

}
