package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cz.msebera.android.httpclient.Header;
@SuppressWarnings("deprecation")
public class HealthArticleViewActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    public DoctorItem mDoctorItem;
    public ImageView mImgArticle;
    public TextView mTxtTitle;
    public TextView mTxtContent;

    public TextView mTxtDoctorName;
    public TextView mTxtDoctorRoom;
    public TextView mTxtDoctorJob;
    public TextView mTxtAddressHospital;
    public TextView mTxtAddressStreet;
    public TextView mTxtViewMap;
    public ImageView mImgDoctorPhoto;

    public HealthArticleItem healthArticleItem;

    public Drawable mImgPlaceholder;

    public String mHospitalName;
    public String mCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_article_view);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) HealthArticleViewActivity.this.getParent();
        final TabActivity tabActivity = (TabActivity) HealthArticleViewActivity.this.getParent().getParent();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.health_article));
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);

        Intent intent = getIntent();
        healthArticleItem = (HealthArticleItem) intent.getSerializableExtra("Article");

        mImgArticle = (ImageView) findViewById(R.id.img_article);
        mTxtTitle = (TextView) findViewById(R.id.txt_article_title);
        mTxtContent = (TextView) findViewById(R.id.txt_article_content);
        mTxtDoctorName = (TextView) findViewById(R.id.txt_doctor_name);
        mTxtDoctorRoom = (TextView) findViewById(R.id.txt_doctor_room);
        mTxtDoctorJob = (TextView) findViewById(R.id.txt_doctor_job);
        mTxtAddressHospital = (TextView) findViewById(R.id.txt_address_hospital);
        mTxtAddressStreet = (TextView) findViewById(R.id.txt_address_street);
        mTxtViewMap = (TextView) findViewById(R.id.txt_view_map);
        mImgDoctorPhoto = (ImageView) findViewById(R.id.img_doctor_photo);

        Picasso
                .with(mContext)
                .load(healthArticleItem.getmImage())
                .into(mImgArticle);
        mTxtTitle.setText(healthArticleItem.getmTitle());

        TextView txtAppoint = (TextView) findViewById(R.id.txt_appoint);
        txtAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tabActivity, ChooseAppointDoctorActivity.class);
                intent.putExtra("Doctor", mDoctorItem);
                tabActivity.startActivity(intent);
                tabActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });

        mTxtViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tabActivity, BaiduMapActivity.class);
                intent.putExtra("hospital", mHospitalName);
                intent.putExtra("city", mCityName);
                tabActivity.startActivity(intent);
                tabActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });
        getArticle();
    }

    private void getArticle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, HealthArticleViewActivity.this);
                RequestParams params = new RequestParams();
                params.put("id", healthArticleItem.getmArticleID());
                params.put("lang", CommonUtils.mIntLang);

                String url = "get_health_article_content";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                JSONObject article = response.getJSONObject("article");
                                mTxtContent.setText(article.getString("contents"));
                                mDoctorItem = new DoctorItem(article.getLong("CourseNo"), article.getLong("WHospNo"), article.getLong("HosNo"),
                                        article.getString("Picture"), article.getString("CourseName"), article.getString("HospName"),
                                        article.getString("HosName"), article.getString("Level"), article.getString("Price"),
                                        article.getString("HospTel"), 5, true);
                                Picasso
                                        .with(mContext)
                                        .load(mDoctorItem.getmPhoto())
                                        .placeholder(mImgPlaceholder)
                                        .transform(CommonUtils.getTransformation(mContext))
                                        .into(mImgDoctorPhoto);
                                mTxtDoctorName.setText(mDoctorItem.getmName());
                                mTxtDoctorRoom.setText(mDoctorItem.getmRoom());
                                mTxtAddressHospital.setText(mDoctorItem.getmHospital());
                                mTxtAddressStreet.setText(article.getString("Address"));
                                mHospitalName = mDoctorItem.getmHospital();
                                mCityName = article.getString("City");
                                //mCityName = "龙井";
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
}
