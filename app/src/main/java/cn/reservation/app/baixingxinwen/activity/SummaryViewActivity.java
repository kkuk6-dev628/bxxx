package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
*/
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cz.msebera.android.httpclient.Header;

public class SummaryViewActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    private TextView mBtnAppoint;
    private DoctorItem mDoctorItem;
    private Drawable mImgPlaceholder;
    private TextView mTxtMedicalTime;
    private TextView mTxtSpecial;
    private TextView mTxtSummaryIntroduce;
    private TextView mTxtAddressStreet;
    private ImageView mImgBaiduMap;

    private String mHospitalName;
    private String mCityName;
/*
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private PoiSearch poiSearch;
    private PoiCitySearchOption poiCitySearchOption;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_summary_view);

        mContext = SummaryViewActivity.this;
        res = mContext.getResources();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.view_summary));

        Intent intent = getIntent();
        mDoctorItem = (DoctorItem) intent.getSerializableExtra("Doctor");
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);

        TextView txtDoctorName = (TextView) findViewById(R.id.txt_doctor_name);
        TextView txtDoctorRoom = (TextView) findViewById(R.id.txt_doctor_room);
        TextView txtHospital = (TextView) findViewById(R.id.txt_hospital);
        TextView txtDoctorDegree = (TextView) findViewById(R.id.txt_doctor_degree);
        TextView txtDoctorFee = (TextView) findViewById(R.id.txt_doctor_fee);
        ImageView imgDoctorPhoto = (ImageView) findViewById(R.id.img_doctor_photo);
        Picasso
                .with(mContext)
                .load(mDoctorItem.getmPhoto())
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(imgDoctorPhoto);
        txtDoctorName.setText(mDoctorItem.getmName());
        txtDoctorRoom.setText(mDoctorItem.getmRoom());
        txtHospital.setText(mDoctorItem.getmHospital());
        txtDoctorDegree.setText(mDoctorItem.getmDegree());
        txtDoctorFee.setText(mDoctorItem.getmFee());

        TextView txtAddressHospital = (TextView) findViewById(R.id.txt_address_hospital);
        txtAddressHospital.setText(mDoctorItem.getmHospital());

        mTxtMedicalTime = (TextView) findViewById(R.id.txt_medical_time);
        mTxtSpecial = (TextView) findViewById(R.id.txt_special);
        mTxtSummaryIntroduce = (TextView) findViewById(R.id.txt_summary_introduce);
        mTxtAddressStreet = (TextView) findViewById(R.id.txt_address_street);

        mBtnAppoint = (TextView) findViewById(R.id.btn_appoint);
        mBtnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryViewActivity.this, ChooseAppointDoctorActivity.class);
                intent.putExtra("Doctor", mDoctorItem);
                SummaryViewActivity.this.startActivity(intent);
                SummaryViewActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });

        //initBaiduMap();
        getSummary();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    private void getSummary() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, SummaryViewActivity.this);
                RequestParams params = new RequestParams();
                params.put("whospno", mDoctorItem.getmHospitalID());
                params.put("hosno", mDoctorItem.getmRoomID());
                params.put("courseno", mDoctorItem.getmDoctorID());
                params.put("lang", CommonUtils.mIntLang);

                String url = "getportfolio";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                CommonUtils.dismissProgress(progressDialog);
                                JSONObject profile = response.getJSONObject("profile");
                                String t = CommonUtils.getFormattedTime(mContext, profile.getString("StartTime"), "00");
                                t += " - " + CommonUtils.getFormattedTime(mContext, profile.getString("RestEndTime"), "00");
                                mTxtMedicalTime.setText(t);
                                mTxtSpecial.setText(profile.getString("special"));
                                mTxtSummaryIntroduce.setText(profile.getString("description"));
                                mTxtAddressStreet.setText(profile.getString("address"));

                                mHospitalName = profile.getString("HospName");
                                mCityName = profile.getString("city");
                                //loadMap(mCityName, mHospitalName);
                            } else {
                                CommonUtils.dismissProgress(progressDialog);
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
        }, 500);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
/*
    private void initBaiduMap() {
        mMapView = (MapView) findViewById(R.id.baidumap_view);
        mBaiduMap = mMapView.getMap();

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.zoomTo(19.0f);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
    }

    private void loadMap(String cityName, String hospitalName) {
        poiCitySearchOption = new PoiCitySearchOption()
                .city(cityName)
                .keyword(hospitalName);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                poiSearch.searchInCity(poiCitySearchOption);
            }
        }, 1000);
    }

    OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            // TODO Auto-generated method stub
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show();
                return;
            }

            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                mBaiduMap.clear();
                MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
                poiOverlay.setData(poiResult);// 设置POI数据
                mBaiduMap.setOnMarkerClickListener(poiOverlay);
                poiOverlay.addToMap();// 将所有的overlay添加到地图上
                poiOverlay.zoomToSpan();
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            } else {// 正常返回结果的时候，此处可以获得很多相关信息
                Toast.makeText(
                        mContext,
                        poiDetailResult.getName() + ": "
                                + poiDetailResult.getAddress(),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            // 检索poi详细信息
            poiSearch.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(poiInfo.uid));
            return true;
        }
    }
    */
}
