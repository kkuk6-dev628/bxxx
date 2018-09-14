package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class UpdateActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener{
    private static String TAG = UpdateActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private LinearLayout parentLinearLayout;
    private LinearLayout thumbLinearLayout;
    private String fid;
    private Integer sortid;

    private LinearLayout mGallery;
    private int[] mImgIds;
    private String[] selProperty;
    private String selPropertyName;
    private int selPropertyIndex;
    private String selPropertyName_second;
    private String selPropertyName_secondVal;
    private String selPropertyName_thirdVal;
    private String selTitleProperty;
    private String selTitleProperty1;
    private String selTitleProperty2;
    private Bitmap[] mSelectedBMP;
    private File[] mFiles;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;
    private int showAction;
    private long tid;
    private Intent pictureActionIntent = null;
    private String userID;
    Bitmap bitmap;
    private JSONObject[] arrayObj;
    String selectedImagePath = null;
    private Dictionary data1;
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(UpdateActivity.this);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        this.data1 = new Hashtable();
        showAction = 0;
        mInflater = LayoutInflater.from(this);
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));
        System.out.println(userID);
        initData();
        initView();
        CommonUtils.data1 = new Hashtable();
        mFiles =new File[10];
        String postItem = (String) intent.getSerializableExtra("PostItem");
        fid = (String) intent.getSerializableExtra("fid");
        sortid = Integer.parseInt((String) intent.getSerializableExtra("sortid"));
        tid = Long.parseLong((String) intent.getSerializableExtra("tid"));
        parentLinearLayout = (LinearLayout) findViewById(R.id.post_parentcontent);
        parentLinearLayout.removeAllViews();
        thumbLinearLayout = (LinearLayout) findViewById(R.id.lyt_upload_thumb);
        System.out.println("fffId:"+fid);
        System.out.println("sortIdddd:"+sortid);
        System.out.println(tid);
        //parentLinearLayout.removeAllViewsInLayout();
        selPropertyName_second = "";
        selPropertyName_thirdVal = "";
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) UpdateActivity.this.getParent();
        setData();
        getPostOption();
        ImageView img_ic_upload_thumb = (ImageView) findViewById(R.id.img_ic_upload_thumb);
        img_ic_upload_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImg();
            }
        });
        ImageView img_detail = (ImageView) findViewById(R.id.img_detail);
        img_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImg();
            }
        });
    }
    
    private String getRealRegion(JSONObject regionObj, String regionID){
        String[] separated = regionID.split("\\.");
        String mainRegion = null;
        try {
            mainRegion = regionObj.getJSONObject("main").optString(separated[0]);
            if(separated.length > 1){
                return mainRegion + " " + regionObj.getJSONObject("yanji").optString(separated[1]);
            }
            else{
                return mainRegion;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
        
    }
    
    
    public void setData(){
        String title ="信息发布更新";
        RelativeLayout rlt_post_data;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView1;
        EditText villiage_edit;
        EditText square_edit;
        EditText brand_edit;
        EditText speed_edit;
        EditText year_edit;
        LinearLayout lyt_post_picture = (LinearLayout) findViewById(R.id.lyt_post_picture);
        LinearLayout.LayoutParams params;
        if(fid!=null && sortid!=null){
            if(fid.equals("2") && sortid==1) {
                fid = "2";
                sortid = 1;
                title = "房屋出售信息发布";
                View rowView2 = inflater.inflate(R.layout.activity_post_item0, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView2, parentLinearLayout.getChildCount() - 1);
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView2.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData1();
                    }
                });
                villiage_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7));
                villiage_edit.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0){
                            EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                            String square = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).getText().toString();
                            String house_number = CommonUtils.data1.get("house_number_desc").toString();
                            //String s_title = title.getText().toString();
                            title.setText(s + " " + house_number + " " + square);
                        }
                    }
                });
                square_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9));
                square_edit.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0){
                            EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                            String v = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).getText().toString();
                            String house_number = CommonUtils.data1.get("house_number_desc").toString();
                            //String s_title = title.getText().toString();
                            title.setText(v + " " + house_number + " " + s);
                        }
                    }
                });
            }else if(fid.equals("2") && sortid==56) {
                fid = "2";
                sortid = 56;
                title = "房屋求购信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item0, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, parentLinearLayout.getChildCount() - 1);
                initPostData();
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData1();
                    }
                });
                villiage_edit = (EditText) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7));
                villiage_edit.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (s.length() != 0) {
                            EditText title = (EditText) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                            String square = ((EditText) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).getText().toString();
                            String house_number = data1.get("house_number_desc").toString();
                            //String s_title = title.getText().toString();
                            title.setText(s + " " + house_number + " " + square);
                        }
                    }
                });
                square_edit = (EditText) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9));
                square_edit.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (s.length() != 0) {
                            EditText title = (EditText) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                            String v = ((EditText) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).getText().toString();
                            String house_number = data1.get("house_number_desc").toString();
                            //String s_title = title.getText().toString();
                            title.setText(v + " " + house_number + " " + s);
                        }
                    }
                });
            }
            else if(fid.equals("2") && sortid==4) {
                title = "房屋出租信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item1, null);
                fid = "2";
                sortid = 4;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData1();
                    }
                });
                villiage_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7));
                villiage_edit.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0){
                            EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                            String square = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).getText().toString();
                            String house_number = CommonUtils.data1.get("house_number_desc").toString();
                            //String s_title = title.getText().toString();
                            title.setText(s + " " + house_number + " " + square);
                        }
                    }
                });
                square_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9));
                square_edit.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0){
                            EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                            String v = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).getText().toString();
                            String house_number = CommonUtils.data1.get("house_number_desc").toString();
                            //String s_title = title.getText().toString();
                            title.setText(v + " " + house_number + " " + s);
                        }
                    }
                });
            }
            else if(fid.equals("2") && sortid==9) {
                title = "房屋求租信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item1, null);
                fid = "2";
                sortid = 9;
                // Add the new row before the add field button.
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData1();
                    }
                });
            }
            else if(fid.equals("93") && sortid==33) {
                title = "店铺出兑信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item2, null);
                fid = "93";
                sortid = 33;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                parentLinearLayout.addView(rowView1, 0);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData3();
                    }
                });
            }else if(fid.equals("38") && sortid==7) {
                title = "招兼职信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item3, null);
                parentLinearLayout.addView(rowView1, 0);
                fid = "38";
                sortid = 7;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData4();
                    }
                });
            }else if(fid.equals("38") && sortid==28) {
                title = "求兼职信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item3, null);
                parentLinearLayout.addView(rowView1, 0);
                fid = "38";
                sortid = 28;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData4();
                    }
                });
            }else if(fid.equals("38") && sortid==53) {
                title = "招聘全职信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item3, null);
                parentLinearLayout.addView(rowView1, 0);
                fid = "38";
                sortid = 53;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData4();
                    }
                });
            }else if(fid.equals("38") && sortid==8) {
                title = "求职简历信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item4, null);
                parentLinearLayout.addView(rowView1, 0);
                fid = "38";
                sortid = 8;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData4();
                    }
                });
            }else if(fid.equals("42") && sortid==13) {
                title = "便民服务信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item5, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "42";
                sortid = 13;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData5();
                    }
                });
            }else if(fid.equals("42") && sortid==34) {
                title = "便民求助信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item5, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "42";
                sortid = 34;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData5();
                    }
                });
            }else if(fid.equals("39")) {
                rowView1 = inflater.inflate(R.layout.activity_post_item6, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                if(sortid == 58) {
                    /*
                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    lyt_post_picture.setLayoutParams(params);
                    lyt_post_picture.setVisibility(View.VISIBLE);
                    */
                    lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                    brand_edit = (EditText)(findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5);
                    brand_edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                                String speed = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).getText().toString();
                                String year = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).getText().toString();
                                //String s_title = title.getText().toString();
                                title.setText(s + " " + year + " " + speed);
                            }
                        }
                    });
                    speed_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4));
                    speed_edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                                String brand = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).getText().toString();
                                String year = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).getText().toString();
                                //String s_title = title.getText().toString();
                                title.setText(brand + " " + year + " " + s);
                            }
                        }
                    });
                    year_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year));
                    year_edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                                String brand = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).getText().toString();
                                String speed = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).getText().toString();
                                //String s_title = title.getText().toString();
                                title.setText(brand + " " + s + " " + speed);
                            }
                        }
                    });
                    title = "车辆出租信息更新";
                }
                else if(sortid==2) {
                    title = "车辆出售信息更新";
                    /*
                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    lyt_post_picture.setLayoutParams(params);
                    lyt_post_picture.setVisibility(View.INVISIBLE);
                    */
                    lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                    brand_edit = (EditText)(findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5);
                    brand_edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                                String speed = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).getText().toString();
                                String year = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).getText().toString();
                                //String s_title = title.getText().toString();
                                title.setText(s + " " + year + " " + speed);
                            }
                        }
                    });
                    speed_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4));
                    speed_edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                                String brand = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).getText().toString();
                                String year = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).getText().toString();
                                //String s_title = title.getText().toString();
                                title.setText(brand + " " + year + " " + s);
                            }
                        }
                    });
                    year_edit = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year));
                    year_edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                                String brand = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).getText().toString();
                                String speed = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).getText().toString();
                                //String s_title = title.getText().toString();
                                title.setText(brand + " " + s + " " + speed);
                            }
                        }
                    });
                }
                else if(sortid==30) {
                    title = "车辆求购信息发布";
                    /*
                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0
                    );
                    lyt_post_picture.setLayoutParams(params);
                    lyt_post_picture.setVisibility(View.INVISIBLE);
                    */
                    lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                }
                else if(sortid==57) {
                    title = "车辆求租信息发布";
                    /*
                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0
                    );
                    lyt_post_picture.setLayoutParams(params);
                    lyt_post_picture.setVisibility(View.INVISIBLE);
                    */
                    lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                }
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData6();
                    }
                });
            }else if(fid.equals("40") && sortid==3) {
                title = "二手出售信息发布";
                View rowView8 = inflater.inflate(R.layout.activity_post_item7, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView8, 0);
                fid = "40";
                sortid = 3;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView8.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData7();
                    }
                });
            }else if(fid.equals("40") && sortid==29) {
                title = "二手求购信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item7, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "40";
                sortid = 29;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                lyt_post_picture.setVisibility(View.INVISIBLE);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData7();
                    }
                });
            }else if(fid.equals("107")) {
                title = "打折促销信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item8, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("44")) {
                title = "招商加盟信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item9, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("48")) {
                title = "婚姻交友信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item10, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("74") && sortid==21) {
                title = "教育培训信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item11, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "74";
                sortid = 21;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("94") && sortid==40) {
                title = "求购号码信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item12, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "94";
                sortid = 40;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("94") && sortid==41) {
                title = "出售号码信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item12, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "94";
                sortid = 41;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("83") && sortid==24) {
                title = "出国资讯信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item13, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "83";
                sortid = 24;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.INVISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.no_upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("92") && sortid==32) {
                title = "宠物天地信息发布";
                rowView1 = inflater.inflate(R.layout.activity_post_item14, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView1, 0);
                fid = "92";
                sortid = 32;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView1.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }else if(fid.equals("50") && sortid==61){
                title = "旅游专栏信息发布";
                View rowView16 = inflater.inflate(R.layout.activity_post_item15, null);
                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView16, 0);
                fid = "50";
                sortid = 61;
                /*
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lyt_post_picture.setLayoutParams(params);
                lyt_post_picture.setEnabled(true);
                lyt_post_picture.setVisibility(View.VISIBLE);
                */
                lyt_post_picture.findViewById(R.id.img_detail).setBackgroundResource(R.drawable.upload_img);
                initPostData();
                rlt_post_data = (RelativeLayout) rowView16.findViewById(R.id.rlt_post_data);
                rlt_post_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postData8();
                    }
                });
            }
        }else{
            final View rowView0 = inflater.inflate(R.layout.activity_post_item1, null);
            parentLinearLayout.removeAllViews();
            parentLinearLayout.removeAllViewsInLayout();
            // Add the new row before the add field button.
            parentLinearLayout.addView(rowView0, 0);
        }
        CommonUtils.customActionBar(mContext, this, true, "编辑");
    }
    public void getData0(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                //tid = response.optJSONObject("ret").optLong("tid");
                                System.out.println(response);
                                //JSONObject list = response.getJSONObject("option");
                                String region = response.optJSONObject("ret").optString("region");
                                String house_number = response.optJSONObject("ret").optString("house_number");
                                String havesun = response.optJSONObject("ret").optString("havesun");
                                String house_level = response.optJSONObject("ret").optString("house_level");
                                String house_type = response.optJSONObject("ret").optString("house_type");
                                String square_range = response.optJSONObject("ret").optString("square_range");
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("house_number",house_number);
                                CommonUtils.data1.put("havesun",havesun);
                                CommonUtils.data1.put("house_level",house_level);
                                CommonUtils.data1.put("house_type",house_type);
                                CommonUtils.data1.put("square_range",square_range);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(house_number));
                                    CommonUtils.data1.put("house_number_desc",arrayObj[1].optString(house_number));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(havesun));
                                }
                                if(arrayObj!=null && arrayObj.length>3 && arrayObj[3]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[3].optString(house_level));
                                }
                                if(arrayObj!=null && arrayObj.length>4 && arrayObj[4]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data5))).setText("" + arrayObj[4].optString(house_type));
                                }
                                if(arrayObj!=null && arrayObj.length>5 && arrayObj[5]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data8))).setText("" + arrayObj[5].optString(square_range));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).setText(response.optJSONObject("ret").optString("villiage"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data11))).setText(response.optJSONObject("ret").optString("floors"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).setText(response.optJSONObject("ret").optString("square"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data10))).setText(response.optJSONObject("ret").optString("price"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
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
        }, 5);
    }
    public Bitmap showImageTwo(String surl) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap bm=null;
        try {
            URL url = new URL(surl);
            bm = BitmapFactory.decodeStream((InputStream)url.getContent());
        } catch (IOException e) {
            System.out.println("e++"+e.getMessage());
            //Log.e(TAG, e.getMessage());
        }
        return bm;
    }
    public void getData1(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                //tid = response.optJSONObject("ret").optLong("tid");
                                System.out.println(response);
                                //JSONObject list = response.getJSONObject("option");
                                String region = response.optJSONObject("ret").optString("region");
                                String house_number = response.optJSONObject("ret").optString("house_number");
                                String award_method = response.optJSONObject("ret").optString("award_method");
                                String havesun = response.optJSONObject("ret").optString("havesun");
                                String house_level = response.optJSONObject("ret").optString("house_level");
                                String house_type = response.optJSONObject("ret").optString("house_type");
                                String square_range = response.optJSONObject("ret").optString("square_range");
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("house_number",house_number);
                                CommonUtils.data1.put("award_method",award_method);
                                CommonUtils.data1.put("havesun",havesun);
                                CommonUtils.data1.put("house_level",house_level);
                                CommonUtils.data1.put("house_type",house_type);
                                CommonUtils.data1.put("square_range",square_range);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(house_number));
                                    CommonUtils.data1.put("house_number_desc",arrayObj[1].optString(house_number));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(award_method));
                                }
                                if(arrayObj!=null && arrayObj.length>3 && arrayObj[3]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[3].optString(havesun));
                                }
                                if(arrayObj!=null && arrayObj.length>4 && arrayObj[4]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data5))).setText("" + arrayObj[4].optString(house_level));
                                }
                                if(arrayObj!=null && arrayObj.length>5 && arrayObj[5]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data6))).setText("" + arrayObj[5].optString(house_type));
                                }
                                if(arrayObj!=null && arrayObj.length>6 && arrayObj[6]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data8))).setText("" + arrayObj[6].optString(square_range));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).setText(response.optJSONObject("ret").optString("villiage"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data11))).setText(response.optJSONObject("ret").optString("floors"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).setText(response.optJSONObject("ret").optString("square"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data10))).setText(response.optJSONObject("ret").optString("price"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic1++" + i + pic.optString(i));
                                        String img_url = "http://bbs.bxxx.cn/data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);//CommonUtils.getBitmapFromURL(img_url);
                                        if(bmp!=null) {
                                            System.out.println("bmp");
                                            storeImageTosdCard(bmp);
                                        }
                                    }
                                }
                                CommonUtils.dismissProgress(progressDialog);
                            }

                        } catch (JSONException e) {
                            CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData2(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String type = response.optJSONObject("ret").optString("type");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data3))).setText(response.optJSONObject("ret").optString("price"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData3(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String education = response.optJSONObject("ret").optString("education");
                                String nation = response.optJSONObject("ret").optString("nation");
                                String salary_range = response.optJSONObject("ret").optString("salary_range");
                                String experience = response.optJSONObject("ret").optString("experience");
                                String level = response.optJSONObject("ret").optString("level");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("education",education);
                                CommonUtils.data1.put("nation",nation);
                                CommonUtils.data1.put("salary_range", salary_range);
                                CommonUtils.data1.put("experience", experience);
                                CommonUtils.data1.put("level", level);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                    CommonUtils.data1.put("region_desc",getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(education));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(nation));
                                }
                                if(arrayObj!=null && arrayObj.length>3 && arrayObj[3]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[3].optString(salary_range));
                                }
                                if(arrayObj!=null && arrayObj.length>4 && arrayObj[4]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data5))).setText("" + arrayObj[4].optString(experience));
                                }
                                if(arrayObj!=null && arrayObj.length>5 && arrayObj[5]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data6))).setText("" + arrayObj[5].optString(level));
                                    CommonUtils.data1.put("level_desc",arrayObj[5].optString(level));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData4(){ //便民服务
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String type = response.optJSONObject("ret").optString("type");
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData5(){ //车辆交易
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++44++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String car_type = response.optJSONObject("ret").optString("car_type");
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("car_type",car_type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(car_type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).setText(response.optJSONObject("ret").optString("car_speed"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).setText(response.optJSONObject("ret").optString("car_brand"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_strong))).setText(response.optJSONObject("ret").optString("abaility_estimate"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).setText(response.optJSONObject("ret").optString("years_estimate"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setText(response.optJSONObject("ret").optString("price"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData6(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++66++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String type = response.optJSONObject("ret").optString("type");
                                String level = response.optJSONObject("ret").optString("level");
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("type",type);
                                CommonUtils.data1.put("level", level);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(level));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setText(response.optJSONObject("ret").optString("price"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
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
        }, 5);
    }
    public void getData7(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String group = response.optJSONObject("ret").optString("group");
                                String type = response.optJSONObject("ret").optString("type");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("group",group);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(group));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData8(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String group = response.optJSONObject("ret").optString("group");
                                String type = response.optJSONObject("ret").optString("type");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("group",group);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(group));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setText(response.optJSONObject("ret").optString("price"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData9(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++11++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String sex = response.optJSONObject("ret").optString("sex");
                                String type = response.optJSONObject("ret").optString("type");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("sex",sex);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(sex));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("name"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_age))).setText(response.optJSONObject("ret").optString("age"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData10(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++1010++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String group = response.optJSONObject("ret").optString("group");
                                String period = response.optJSONObject("ret").optString("period");
                                String type = response.optJSONObject("ret").optString("type");
                                String price = response.optJSONObject("ret").optString("price");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("group",group);
                                CommonUtils.data1.put("period",period);
                                CommonUtils.data1.put("type",type);
                                CommonUtils.data1.put("price",price);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(group));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(period));
                                }
                                if(arrayObj!=null && arrayObj.length>3 && arrayObj[3]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[3].optString(type));
                                }
                                if(arrayObj!=null && arrayObj.length>4 && arrayObj[4]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data5))).setText("" + arrayObj[4].optString(price));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData11(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++1111++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String company = response.optJSONObject("ret").optString("company");
                                String type = response.optJSONObject("ret").optString("type");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("company",company);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(company));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setText(response.optJSONObject("ret").optString("price"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData12(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++1111++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String group = response.optJSONObject("ret").optString("group");
                                String period = response.optJSONObject("ret").optString("period");
                                String type = response.optJSONObject("ret").optString("type");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("group",group);
                                CommonUtils.data1.put("period",period);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(group));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(period));
                                }
                                if(arrayObj!=null && arrayObj.length>3 && arrayObj[3]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[3].optString(type));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData13(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++1111++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String source = response.optJSONObject("ret").optString("source");
                                String type = response.optJSONObject("ret").optString("type");
                                String award_method = response.optJSONObject("ret").optString("award_method");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("source",source);
                                CommonUtils.data1.put("award_method",award_method);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(source));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(type));
                                }
                                if(arrayObj!=null && arrayObj.length>3 && arrayObj[3]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[3].optString(award_method));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_price))).setText(response.optJSONObject("ret").optString("price"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void getData14(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("tid",tid);
                String url = "news/info";
                System.out.println("tid++1111++"+tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                System.out.println(response);
                                String region = response.optJSONObject("ret").optString("region");
                                String type = response.optJSONObject("ret").optString("type");
                                String order = response.optJSONObject("ret").optString("order");
                                System.out.println(arrayObj[0]);
                                CommonUtils.data1.put("region",region);
                                CommonUtils.data1.put("order",order);
                                CommonUtils.data1.put("type",type);
                                if(arrayObj!=null && arrayObj.length>0 && arrayObj[0]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + getRealRegion(arrayObj[0], region));
                                }
                                if(arrayObj!=null && arrayObj.length>1 && arrayObj[1]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[1].optString(type));
                                }
                                if(arrayObj!=null && arrayObj.length>2 && arrayObj[2]!=null) {
                                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[2].optString(order));
                                }
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setText(response.optJSONObject("ret").optString("title"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setText(response.optJSONObject("ret").optString("message"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setText(response.optJSONObject("ret").optString("contact"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setText(response.optJSONObject("ret").optString("phone"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).setText(response.optJSONObject("ret").optString("qq"));
                                ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).setText(response.optJSONObject("ret").optString("telephone"));
                                JSONArray pic = response.optJSONObject("ret").optJSONArray("picture");
                                if(pic!=null) {
                                    for (int i = 0; i < pic.length(); i++) {
                                        System.out.println("pic++" + i + pic.optString(i));
                                        String img_url = APIManager.Sever_URL + "data/attachment/forum/"+ pic.optString(i);
                                        System.out.println(img_url);
                                        Bitmap bmp = showImageTwo(img_url);
                                        if(bmp!=null)
                                            storeImageTosdCard(bmp);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 5);
    }
    public void postData1(){
        if (validateData1()==false){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData1(){

        String villiage = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).getText().toString();
        String square = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).getText().toString();
        System.out.println("villiage++++++++++"+villiage);
        CommonUtils.mFiles = mFiles;
        CommonUtils.data1.put("villiage",villiage);
        CommonUtils.data1.put("square",square);
        CommonUtils.data1.put("price",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data10))).getText());
        CommonUtils.data1.put("floors",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data11))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        //CommonUtils.data1.put("house_number","");
        if ((sortid ==1 || sortid==4) && (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0]==null)) {
            Toast toast = Toast.makeText(mContext, "请上传图片", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(villiage.trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入小区名称", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("square").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入面积", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("price").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入售价", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data10))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data10))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("floors").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入楼层", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data11))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data11))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("contact").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("house_number").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择户型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("house_type").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择房屋类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("house_level").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择房屋装修", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("havesun").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择朝向", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        /*
        if(CommonUtils.data1.get("square_range").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择面积范围", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        */
        if((fid.equals("2") && sortid == 9) || (fid.equals("2") && sortid == 4)){

        }
        else if(CommonUtils.data1.get("award_method").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择货款方式", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }
    public void postData3(){
        if (!validateData3()){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData3(){

        //String square = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).getText().toString();
        CommonUtils.data1.put("square","");
        CommonUtils.mFiles = mFiles;
        CommonUtils.data1.put("price",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data3))).getText());
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        if ((fid.equals("93") || sortid==33) && (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0]==null)) {
            Toast toast = Toast.makeText(mContext, "请上传图片", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("price").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入兑价", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data3))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data3))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("contact").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("type").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }
    public void postData4(){
        if (!validateData4()){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData4(){
        CommonUtils.mFiles = mFiles;
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        //CommonUtils.data1.put("house_number","");
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("education").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择学历", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("nation").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择民族", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("salary_range").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择月薪", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("level").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择职位", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("experience").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择经验", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }
    public void postData5(){
        if (!validateData5()){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData5(){
        CommonUtils.mFiles = mFiles;
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        if(CommonUtils.data1.get("contact").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("type").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }
    public void postData6(){
        if (!validateData6()){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData6(){
        CommonUtils.mFiles = mFiles;
        String car_speed = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).getText().toString();
        String car_brand = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).getText().toString();
        String abaility_estimate = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_strong))).getText().toString();
        String years_estimate = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).getText().toString();
        CommonUtils.data1.put("car_brand",car_brand);
        CommonUtils.data1.put("car_speed",car_speed);
        CommonUtils.data1.put("abaility_estimate",abaility_estimate);
        CommonUtils.data1.put("years_estimate",years_estimate);
        CommonUtils.data1.put("price",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        //CommonUtils.data1.put("house_number","");
        if(CommonUtils.data1.get("car_brand").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入品牌车系", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data5))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("car_speed").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入行驶里程", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data4))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("price").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入价格", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_price))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_price))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("abaility_estimate").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入强险到期", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_strong))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_strong))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("years_estimate").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入年检到期", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_year))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("contact").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("car_type").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择车辆类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }
    public void postData7(){
        if (!validateData7()){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData7(){
        CommonUtils.mFiles = mFiles;
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        if ((sortid ==3) && (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0]==null)) {
            Toast toast = Toast.makeText(mContext, "请上传图片", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("contact").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if(CommonUtils.data1.get("type").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        CommonUtils.data1.put("price",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).getText());
        if(CommonUtils.data1.get("price").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入价格", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("level").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择新旧程度", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }
    public void postData8(){
        if (!validateData8()){
            return;
        }
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonUtils.data1.put("userID",userID);
        CommonUtils.data1.put("fId",fid);
        CommonUtils.data1.put("sortId",sortid);
        postData();
    }
    public boolean validateData8(){
        CommonUtils.mFiles = mFiles;
        CommonUtils.data1.put("qq",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data13))).getText());
        CommonUtils.data1.put("contact",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
        CommonUtils.data1.put("phone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).getText());
        CommonUtils.data1.put("telephone",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data15))).getText());
        CommonUtils.data1.put("message",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).getText());
        switch(fid){
            case "107":
                if(CommonUtils.data1.get("type").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("group").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择分类", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("contact").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
                    return false;
                }
                break;
            case "44":
                if(CommonUtils.data1.get("type").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("group").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择分类", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                CommonUtils.data1.put("price",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).getText());
                if(CommonUtils.data1.get("price").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入费用", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).requestFocus();
                    return false;
                }
                if(CommonUtils.data1.get("contact").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
                    return false;
                }
                break;
            case "48":
                if(CommonUtils.data1.get("type").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                CommonUtils.data1.put("age",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_age))).getText());
                if(CommonUtils.data1.get("age").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入年龄", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_age))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_age))).requestFocus();
                    return false;
                }
                CommonUtils.data1.put("name",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).getText());
                if(CommonUtils.data1.get("name").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
                    return false;
                }
                if(CommonUtils.data1.get("sex").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择性别", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                break;
            case "74":
                if(CommonUtils.data1.get("type").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("group").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择分类", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("period").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择周期", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("price").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入学费", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("contact").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
                    return false;
                }
                break;
            case "94":
                if(CommonUtils.data1.get("type").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择号码类型", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("company").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择通讯公司", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                CommonUtils.data1.put("price",((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).getText());
                if(CommonUtils.data1.get("price").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入价格", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data_price))).requestFocus();
                    return false;
                }
                if(CommonUtils.data1.get("contact").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
                    return false;
                }
                break;
            case "83":
                if(CommonUtils.data1.get("type").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择类型", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("group").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择分类", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("period").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请选择周期", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                if(CommonUtils.data1.get("contact").toString().trim().equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入联系人", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).setFocusableInTouchMode(true);
                    ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_contact))).requestFocus();
                    return false;
                }
                break;
            case "92":
                if (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0]==null) {
                    Toast toast = Toast.makeText(mContext, "请上传图片", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
                break;
            case "50":
                if (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0]==null) {
                    Toast toast = Toast.makeText(mContext, "请上传图片", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    return false;
                }
        }
        if(!fid.equals("48") && CommonUtils.data1.get("phone").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data14))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("region").toString().trim().equals("")){
            Toast toast = Toast.makeText(mContext, "请选择地区", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        String title = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).getText().toString();
        CommonUtils.data1.put("title",title);
        if(CommonUtils.data1.get("title").toString().trim().equals("") || CommonUtils.data1.get("title").toString().trim().length()<5){
            Toast toast = Toast.makeText(mContext, "标题5个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title))).requestFocus();
            return false;
        }
        if(CommonUtils.data1.get("message").toString().trim().equals("") || CommonUtils.data1.get("message").toString().trim().length()<10){
            Toast toast = Toast.makeText(mContext, "内容10个字以上", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).setFocusableInTouchMode(true);
            ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_message))).requestFocus();
            return false;
        }
        return true;
    }

    public void initPostData(){
        CommonUtils.mFiles =  new File[0];
        CommonUtils.data1.put("tid", tid);
        CommonUtils.data1.put("source","");
        CommonUtils.data1.put("house_number","");
        CommonUtils.data1.put("floors","");
        CommonUtils.data1.put("contact","");
        CommonUtils.data1.put("phone","");
        CommonUtils.data1.put("region","");
        CommonUtils.data1.put("region_desc","");
        CommonUtils.data1.put("house_type","");
        CommonUtils.data1.put("house_level","");
        CommonUtils.data1.put("house_type","");
        CommonUtils.data1.put("house_level","");
        CommonUtils.data1.put("havesun","");
        CommonUtils.data1.put("villiage","");
        CommonUtils.data1.put("award_method","");
        CommonUtils.data1.put("square","");
        CommonUtils.data1.put("square_range","");
        CommonUtils.data1.put("price","");
        CommonUtils.data1.put("picture","");
        CommonUtils.data1.put("message","");
        CommonUtils.data1.put("title","");
        CommonUtils.data1.put("house_number_desc","");
        CommonUtils.data1.put("numbers","");
        CommonUtils.data1.put("level","");
        CommonUtils.data1.put("level_desc","");
        CommonUtils.data1.put("salary_range","");
        CommonUtils.data1.put("award_period","");
        CommonUtils.data1.put("sex_demand","");
        CommonUtils.data1.put("salary","");
        CommonUtils.data1.put("experience","");
        CommonUtils.data1.put("education","");
        CommonUtils.data1.put("nation","");
        CommonUtils.data1.put("qq","");
        CommonUtils.data1.put("telephone","");
        CommonUtils.data1.put("current_level","");
        CommonUtils.data1.put("ask_region","");
        CommonUtils.data1.put("home_region","");
        CommonUtils.data1.put("name","");
        CommonUtils.data1.put("age","");
        CommonUtils.data1.put("sex","");
        CommonUtils.data1.put("option","");
        CommonUtils.data1.put("company_address","");
        CommonUtils.data1.put("company_name","");
        CommonUtils.data1.put("car_type","");
        CommonUtils.data1.put("car_brand","");
        CommonUtils.data1.put("brand_years","");
        CommonUtils.data1.put("car_speed","");
        CommonUtils.data1.put("car_price","");
        CommonUtils.data1.put("test_estimate","");
        CommonUtils.data1.put("abaility_estimate","");
        CommonUtils.data1.put("brand_years","");
        CommonUtils.data1.put("car_speed","");
        CommonUtils.data1.put("years_estimate","");
        CommonUtils.data1.put("abaility_estimate","");
        CommonUtils.data1.put("group","");
        CommonUtils.data1.put("type","");
        CommonUtils.data1.put("company_address","");
        CommonUtils.data1.put("company_name","");
        CommonUtils.data1.put("age","");
        CommonUtils.data1.put("star_pos","");
        CommonUtils.data1.put("height","");
        CommonUtils.data1.put("weight","");
        CommonUtils.data1.put("education","");
        CommonUtils.data1.put("salary","");
        CommonUtils.data1.put("hobby","");
        CommonUtils.data1.put("future","");
        CommonUtils.data1.put("address","");
        CommonUtils.data1.put("company","");
        CommonUtils.data1.put("region","");
        CommonUtils.data1.put("education","");
        CommonUtils.data1.put("type","");
        CommonUtils.data1.put("period","");
        CommonUtils.data1.put("education_price","");
        CommonUtils.data1.put("carry_period","");
        CommonUtils.data1.put("order","");
    }
    private void getPostOption() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
//                RequestParams params = new RequestParams();
//                params.put("fid", fid);
                if(sortid==null || fid.equals("")) {
                    CommonUtils.dismissProgress(progressDialog);
                    Intent intent = new Intent(UpdateActivity.this, PostCategoryActivity.class);
                    pActivity.startChildActivity("post_category", intent);
                }
//                params.put("sortid", sortid);
//                String url = "news/postoption";
//                System.out.println("sortid++++"+sortid);
//                System.out.println("fid++++"+fid);


                HashMap<String, Object> params1 = new HashMap<String, Object>();
                params1.put("sortid", sortid);
                params1.put("fid", fid);
                String url1 = "api/news/postoption";


                NetRetrofit.getInstance().post(url1, params1, new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                        CommonUtils.dismissProgress(progressDialog);
                        JSONObject response = resp.body();
                        try {
                            if (response.getInt("code") == 1) {
                                JSONObject list = response.getJSONObject("option");
//                                System.out.print(list);
                                initPostOptionView(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                });
//                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        try {
//                            CommonUtils.dismissProgress(progressDialog);
//                            if (response.getInt("code") == 1) {
//                                JSONObject list = response.getJSONObject("option");
//                                System.out.print(list);
//                                initPostOptionView(list);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        progressDialog.dismiss();
//                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        progressDialog.dismiss();
//                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        }, 5);
    }
    private void initPostOptionView(JSONObject list) {
        arrayObj = new JSONObject[7];
        RelativeLayout rlt_input_data1;
        RelativeLayout rlt_input_data2;
        RelativeLayout rlt_input_data3;
        RelativeLayout rlt_input_data4;
        RelativeLayout rlt_input_data5;
        RelativeLayout rlt_input_data6;
        RelativeLayout rlt_input_data7;
        selPropertyName = "";
        selPropertyIndex = -1;
        selTitleProperty = "";
        selTitleProperty1 = "";
        selTitleProperty2 = "";
        if(list!=null && list.length()>0) {
            if(fid.equals("2") && (sortid==4 || sortid==9)) {
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                rlt_input_data4 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data4);
                rlt_input_data5 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data5);
                rlt_input_data6 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data6);
                rlt_input_data7 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data7);
                for (int i = 0; i < list.length(); i++) {
                    //try {
                    //JSONObject item = list.getJSONObject(i);
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        createActionSheet(region_name);
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("house_number");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        createActionSheet(arrayObj[1]);
                                        showAction = 1;
                                        selTitleProperty = "house_number_desc";
                                        selPropertyName = "house_number";
                                        selPropertyIndex = 1;
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("havesun");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        createActionSheet(arrayObj[2]);
                                        showAction = 1;
                                        selPropertyName = "havesun";
                                        selPropertyIndex = 2;
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 3:
                            arrayObj[3] = list.optJSONObject("house_level");
                            rlt_input_data4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        createActionSheet(arrayObj[3]);
                                        showAction = 1;
                                        selPropertyName = "house_level";
                                        selPropertyIndex = 3;
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 4:
                            arrayObj[4] = list.optJSONObject("house_type");
                            rlt_input_data5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "house_type";
                                        selPropertyIndex = 4;
                                        createActionSheet(arrayObj[4]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 5:
                            arrayObj[5] = list.optJSONObject("square_range");
                            rlt_input_data7.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "square_range";
                                        selPropertyIndex = 5;
                                        createActionSheet(arrayObj[5]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                    /*
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                }
                getData0();
            }else if(fid=="2") {
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                rlt_input_data4 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data4);
                rlt_input_data5 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data5);
                rlt_input_data6 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data6);
                rlt_input_data7 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data7);
                for (int i = 0; i < list.length(); i++) {
                    //try {
                    //JSONObject item = list.getJSONObject(i);
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("house_number");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selTitleProperty = "house_number_desc";
                                        selPropertyName = "house_number";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("award_method");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "award_method";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 3:
                            arrayObj[3] = list.optJSONObject("havesun");
                            rlt_input_data4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "havesun";
                                        selPropertyIndex = 3;
                                        createActionSheet(arrayObj[3]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 4:
                            arrayObj[4] = list.optJSONObject("house_level");
                            rlt_input_data5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "house_level";
                                        selPropertyIndex = 4;
                                        createActionSheet(arrayObj[4]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 5:
                            arrayObj[5] = list.optJSONObject("house_type");
                            rlt_input_data6.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "house_type";
                                        selPropertyIndex = 5;
                                        createActionSheet(arrayObj[5]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 6:
                            arrayObj[6] = list.optJSONObject("square_range");
                            rlt_input_data7.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "square_range";
                                        selPropertyIndex = 6;
                                        createActionSheet(arrayObj[6]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                    /*
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                }
                getData1();
            }else if(fid.equals("93") || fid.equals("42")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("type");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                if(fid.equals("93")) {
                    getData2();
                }else if(fid.equals("42")){
                    getData4();
                }
            }else if(fid.equals("38")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                rlt_input_data4 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data4);
                rlt_input_data5 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data5);
                rlt_input_data6 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data6);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selTitleProperty1 = "region_desc";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("education");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "education";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("nation");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "nation";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 3:
                            arrayObj[3] = list.optJSONObject("salary_range");
                            rlt_input_data4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "salary_range";
                                        selPropertyIndex = 3;
                                        createActionSheet(arrayObj[3]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 4:
                            arrayObj[4] = list.optJSONObject("experience");
                            rlt_input_data5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "experience";
                                        selPropertyIndex = 4;
                                        createActionSheet(arrayObj[4]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 5:
                            arrayObj[5] = list.optJSONObject("level");
                            rlt_input_data6.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "level";
                                        selTitleProperty2 = "level_desc";
                                        selPropertyIndex = 5;
                                        createActionSheet(arrayObj[5]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData3();
            }else if(fid.equals("39")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("car_type");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "car_type";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData5();
            }else if(fid.equals("40")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("level");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "level";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("type");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData6();
            }else if(fid.equals("107") || fid.equals("44")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("group");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "group";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("type");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                if(fid.equals("107")){
                    getData7();
                }else if(fid.equals("44")){
                    getData8();
                }
            }else if(fid.equals("48")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("sex");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "sex";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("type");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData9();
            }else if(fid.equals("74")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                rlt_input_data4 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data4);
                rlt_input_data5 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data5);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("group");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "group";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("period");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "period";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 3:
                            arrayObj[3] = list.optJSONObject("type");
                            rlt_input_data4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 3;
                                        createActionSheet(arrayObj[3]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 4:
                            arrayObj[4] = list.optJSONObject("price");
                            rlt_input_data5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "price";
                                        selPropertyIndex = 4;
                                        createActionSheet(arrayObj[4]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData10();
            }else if(fid.equals("94")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("company");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "company";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("type");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData11();
            }else if(fid.equals("83")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                rlt_input_data4 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data4);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("group");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "group";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("period");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "period";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 3:
                            arrayObj[3] = list.optJSONObject("type");
                            rlt_input_data4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 3;
                                        createActionSheet(arrayObj[3]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData12();
            }else if(fid.equals("92")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                rlt_input_data4 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data4);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("source");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "source";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("type");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 3:
                            arrayObj[3] = list.optJSONObject("award_method");
                            rlt_input_data4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "award_method";
                                        selPropertyIndex = 3;
                                        createActionSheet(arrayObj[3]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData13();
            }else if(fid.equals("50")){
                rlt_input_data1 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data1);
                rlt_input_data2 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data2);
                rlt_input_data3 = (RelativeLayout) findViewById(R.id.post_parentcontent).findViewById(R.id.rlt_input_data3);
                for (int i = 0; i < list.length(); i++) {
                    switch (i) {
                        case 0:
                            arrayObj[0] = list.optJSONObject("region");
                            rlt_input_data1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        JSONObject region_name = arrayObj[0].optJSONObject("main");
                                        showAction = 1;
                                        selPropertyName = "region";
                                        selPropertyIndex = 0;
                                        createActionSheet(region_name);
                                    } else {
                                        //actionSheet.dismiss();
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 1:
                            arrayObj[1] = list.optJSONObject("type");
                            rlt_input_data2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "type";
                                        selPropertyIndex = 1;
                                        createActionSheet(arrayObj[1]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        case 2:
                            arrayObj[2] = list.optJSONObject("order");
                            rlt_input_data3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (showAction == 0) {
                                        showAction = 1;
                                        selPropertyName = "order";
                                        selPropertyIndex = 2;
                                        createActionSheet(arrayObj[2]);
                                    } else {
                                        showAction = 0;
                                        selPropertyName = "";
                                        selPropertyIndex = -1;
                                    }
                                }
                            });
                            break;
                        default:

                            break;
                    }
                }
                getData14();
            }
        }
    }
    public void getImg(){
        /*
        System.out.println("Gallery button");
        TabActivity tabActivity = (TabActivity) UpdateActivity.this.getParent().getParent();
        pictureActionIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        tabActivity.startActivityForResult(pictureActionIntent, CommonUtils.GALLERY_PICTURE);
        */
        Intent intent = new Intent(UpdateActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false);
        mResults = null;
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        this.startActivityForResult(intent, CommonUtils.GALLERY_PICTURE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if(requestCode == CommonUtils.GALLERY_PICTURE) {
            if(resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
                StringBuffer sb = new StringBuffer();
                //sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for(String result : mResults) {
                    System.out.println(result);
                    setResult(result);
                    //sb.append(result).append("\n");
                }
                //tvResults.setText(sb.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void setResult(String filePath){
        //Cursor c = getContentResolver().query(selectedImage, filePath,
        //        null, null, null);
        //c.moveToFirst();
        //int columnIndex = c.getColumnIndex(filePath);
        //selectedImagePath = c.getString(columnIndex);

        //c.close();

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        bitmap = BitmapFactory.decodeFile(filePath, options); // load
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        storeImageTosdCard(bitmap);
    }
    public void selectResult(int requestCode, int resultCode, Intent data) {

        bitmap = null;

        if (resultCode == RESULT_OK && requestCode == CommonUtils.CAMERA_REQUEST) {


        } else if (resultCode == RESULT_OK && requestCode == CommonUtils.GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);

                c.close();

                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                bitmap = BitmapFactory.decodeFile(selectedImagePath, options); // load
                bitmap = Bitmap.createScaledBitmap(bitmap, 128, 64, true);

                storeImageTosdCard(bitmap);

            } else {
                Toast.makeText(getApplicationContext(), "取消",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void storeImageTosdCard(Bitmap processedBitmap) {
        try {
            // TODO Auto-generated method stub
            OutputStream output;

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            //Log.e(TAG, "selectedImagePath0:" + String.valueOf(selectedImagePath));
            File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!storageDirectory.exists()) {
                storageDirectory = Environment.getExternalStoragePublicDirectory("data");
            }
            File mediaFile = new File(storageDirectory + File.separator + "IMG_" + timeStamp  + mFiles.length + ".png");

            if (mediaFile.exists()) {
                mediaFile.delete();
                mediaFile.createNewFile();
            } else {
                mediaFile.createNewFile();
            }

            try {

                output = new FileOutputStream(mediaFile);

                // Compress into png format image from 0% - 100%
                processedBitmap
                        .compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();

                int file_size = Integer
                        .parseInt(String.valueOf(mediaFile.length() / 1024));
                System.out.println("size ===>>> " + file_size);
                System.out.println("file.length() ===>>> " + mediaFile.length());

                selectedImagePath = mediaFile.getAbsolutePath();

                ImageView img_ic_upload = (ImageView) findViewById(R.id.img_detail);
                img_ic_upload.setImageResource(android.R.color.transparent);
                img_ic_upload.setImageBitmap(processedBitmap);
                if(mSelectedBMP==null){
                    mSelectedBMP = new Bitmap[] { processedBitmap };
                    mFiles =  new File[1];
                    mFiles[0] = mediaFile;
                }else{
                    Bitmap[] oldSelectBMP = mSelectedBMP;
                    File[] oldFile = mFiles;
                    mSelectedBMP = new Bitmap[oldSelectBMP.length+1];
                    mFiles = new File[oldFile.length+1];
                    for(int i=0;i<oldSelectBMP.length;i++){
                        mSelectedBMP[i] = oldSelectBMP[i];
                        mFiles[i] = oldFile[i];
                    }
                    mSelectedBMP[mSelectedBMP.length-1] = processedBitmap;
                    mFiles[mSelectedBMP.length-1] = mediaFile;
                }
                if(mFiles.length>0){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    thumbLinearLayout.setLayoutParams(params);
                }else{
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0
                    );
                    thumbLinearLayout.setLayoutParams(params);
                }
                initBMPView();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void initData()
    {
        mImgIds = new int[] { };
    }

    private void initView()
    {
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);

        for (int i = 0; i < mImgIds.length; i++)
        {

            View view = mInflater.inflate(R.layout.activity_gallery_item,
                    mGallery, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.id_index_gallery_item_image);
            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view
                    .findViewById(R.id.id_index_gallery_item_text);
            txt.setText("info "+i);
            mGallery.addView(view);
        }
    }
    private void initBMPView()
    {
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        mGallery.removeAllViews();
        for (int i = 0; i < mSelectedBMP.length; i++)
        {

            View view = mInflater.inflate(R.layout.activity_gallery_item,
                    mGallery, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.id_index_gallery_item_image);
            img.setImageResource(android.R.color.transparent);
            img.setImageBitmap(mSelectedBMP[i]);
            img.setId(i);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView img_ic_upload = (ImageView) findViewById(R.id.img_detail);
                    img_ic_upload.setImageResource(android.R.color.transparent);
                    int m_index = view.getId();
                    img_ic_upload.setImageBitmap(mSelectedBMP[m_index]);
                }
            });
            ImageView img_del = (ImageView) view
                    .findViewById(R.id.id_index_gallery_item_del);
            img_del.setId(i);
            img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int m_index = view.getId();
                    Bitmap[] oldSelectBMP = mSelectedBMP;
                    mSelectedBMP = new Bitmap[oldSelectBMP.length-1];
                    File[] oldFile = mFiles;
                    mSelectedBMP = new Bitmap[oldSelectBMP.length-1];
                    mFiles = new File[oldSelectBMP.length-1];
                    int j = 0;
                    for(int i=0;i<oldSelectBMP.length;i++){
                        if(i!=m_index) {
                            mSelectedBMP[j] = oldSelectBMP[i];
                            mFiles[j] = oldFile[i];
                            j++;
                        }
                    }
                    if(mFiles.length==0){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0
                        );
                        thumbLinearLayout.setLayoutParams(params);
                        ImageView img_ic_upload = (ImageView) findViewById(R.id.img_detail);
                        img_ic_upload.setImageResource(android.R.color.transparent);
                    }
                    initBMPView();
                }
            });
            mGallery.addView(view);
            ImageView img_ic_upload = (ImageView) findViewById(R.id.img_detail);
            img_ic_upload.setImageResource(android.R.color.transparent);
            img_ic_upload.setImageBitmap(mSelectedBMP[i]);
        }
    }
    public void createActionSheet(JSONObject state_name){
        if(state_name==null || (state_name!=null && state_name.length()==0)){
            return;
        }
        String[] arr = new String[state_name.length()];
        Integer j = 0;
        for(int i=0; i < state_name.length(); i++){
            j++;
            arr[i] = state_name.optString(j.toString());
        }
        selProperty = arr;
        ActionSheet.createBuilder(mContext, UpdateActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle(res.getString(R.string.str_cancel))
                .setOtherButtonTitles(arr)
                .setCancelableOnTouchOutside(true)
                .setListener(UpdateActivity.this)
                .show();
    }
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.activity_post_item1, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        showAction=0;
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        showAction=0;
        CommonUtils.data1.put(selPropertyName, index+1);
        String val = String.valueOf(index+1);
        if(arrayObj==null || arrayObj.length<=selPropertyIndex || arrayObj[selPropertyIndex].length()<1){
            return;
        }
        CommonUtils.data1.put(selPropertyName, index+1);
        if(selPropertyName.equals("region") && selPropertyName_second.equals("yanji")) {
            int v_index = index + 1;
            String s_index = CommonUtils.data1.get(selPropertyName).toString()+"."+v_index;
            CommonUtils.data1.put(selPropertyName, s_index);
            JSONObject region_name = arrayObj[0].optJSONObject("yanji");
            selPropertyName_thirdVal = selPropertyName_secondVal +" "+region_name.optString(val);
        }else if(selPropertyName.equals("region")){
            JSONObject region_name = arrayObj[0].optJSONObject("main");
            selPropertyName_secondVal = region_name.optString(val);
            selPropertyName_thirdVal = region_name.optString(val);
        }
        if(arrayObj==null || arrayObj.length<=selPropertyIndex || (selPropertyIndex>-1 && arrayObj[selPropertyIndex].length()<1)){
            return;
        }
        System.out.println("region++"+selPropertyName + "::"+selPropertyName_second);
        if(selPropertyName.equals("region") && index==0 && !selPropertyName_second.equals("yanji")){
            System.out.println("region++"+arrayObj[selPropertyIndex]);
            JSONObject region_second = arrayObj[selPropertyIndex].optJSONObject("yanji");
            if(region_second!=null && region_second.length()>0) {
                String[] arr = new String[region_second.length()];
                Integer j = 0;
                for (int i = 0; i < region_second.length(); i++) {
                    System.out.println("Region Second" + i + region_second.optString(j.toString()));
                    j = i+1;
                    arr[i] = region_second.optString(j.toString());
                }
                selPropertyName_second = "yanji";
                actionSheet.dismiss();
                ActionSheet.createBuilder(mContext, UpdateActivity.this.getSupportFragmentManager())
                        .setCancelButtonTitle(res.getString(R.string.str_cancel))
                        .setOtherButtonTitles(arr)
                        .setCancelableOnTouchOutside(true)
                        .setListener(UpdateActivity.this)
                        .show();
                return;
            }else{
                selPropertyName_second = "";
            }
        }else{
            selPropertyName_second = "";
        }
        if(selPropertyName.equals("region")){
            ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + selPropertyName_thirdVal);
        }else{
            switch (selPropertyIndex) {
                case 0:
                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    break;
                case 1:
                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    break;
                case 2:
                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    break;
                case 3:
                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    break;
                case 4:
                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data5))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    break;
                case 5:
                    System.out.println(fid);
                    System.out.println(sortid);
                    if (fid.equals("2") && (sortid == 4 || sortid == 9)) {
                        ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data8))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    } else {
                        ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data6))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    }
                    break;
                case 6:
                    ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data8))).setText("" + arrayObj[selPropertyIndex].optString(val));
                    break;
            }
        }
        /*
        switch (selPropertyIndex){
            case 0:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data1))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
            case 1:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data2))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
            case 2:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data3))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
            case 3:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data4))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
            case 4:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data5))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
            case 5:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data6))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
            case 6:
                ((TextView) ((findViewById(R.id.post_parentcontent)).findViewById(R.id.txt_input_data8))).setText("" + arrayObj[selPropertyIndex].optString(val));
                break;
        }
        */
        switch (fid){
            case "2":
                if(selTitleProperty!=""){
                    CommonUtils.data1.put(selTitleProperty, selProperty[index]);
                    EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                    String square = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data9))).getText().toString();
                    String v = ((EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_data7))).getText().toString();
                    title.setText(v + " " + selProperty[index] + " " + square);
                    selTitleProperty = "";
                }
                break;
            case "38":
                if(selTitleProperty1!=""){
                    CommonUtils.data1.put(selTitleProperty1, selProperty[index]);
                    EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                    title.setText(selProperty[index]+ " " +CommonUtils.data1.get("level_desc"));
                    selTitleProperty1 = "";
                }
                if(selTitleProperty2!=""){
                    CommonUtils.data1.put(selTitleProperty2, selProperty[index]);
                    EditText title = (EditText)((findViewById(R.id.post_parentcontent)).findViewById(R.id.edit_input_title));
                    title.setText(CommonUtils.data1.get("region_desc") + " " + selProperty[index]);
                    selTitleProperty2 = "";
                }
                break;
        }
        actionSheet.dismiss();
    }
    public void postData(){
        RelativeLayout rlt_post_data = (RelativeLayout) findViewById(R.id.rlt_post_data);
        rlt_post_data.setEnabled(false);
        Toast.makeText(mContext, "发布中", Toast.LENGTH_SHORT).show();
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                String url = "/post/limit";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        System.out.println("limit_post++"+response);
                        if (response.optString("ret").equals("1")) {
                            freePostData();
                        }else{
                            finish();
                            Intent intent = new Intent(UpdateActivity.this, ChargeUpdateActivity.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("fid", fid);
                            intent.putExtra("sortid", sortid.toString());
                            UpdateActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_ANOTHER);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 20);
    }
    public void freePostData(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UpdateActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("tid", CommonUtils.data1.get("tid"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("action", "update");
                switch(CommonUtils.data1.get("fId").toString()){
                    case "2":
                        params.put("source", CommonUtils.data1.get("source"));
                        params.put("house_number", CommonUtils.data1.get("house_number"));
                        params.put("floors", CommonUtils.data1.get("floors"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("house_type", CommonUtils.data1.get("house_type"));
                        params.put("house_level", CommonUtils.data1.get("house_level"));
                        params.put("havesun", CommonUtils.data1.get("havesun"));
                        params.put("villiage", CommonUtils.data1.get("villiage"));
                        params.put("award_method", CommonUtils.data1.get("award_method"));
                        params.put("square", CommonUtils.data1.get("square"));
                        params.put("square_range", CommonUtils.data1.get("square_range"));
                        params.put("price", CommonUtils.data1.get("price"));
                        break;
                    case "93":
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("price", CommonUtils.data1.get("price"));
                        break;
                    case "38": //招兼职信息发布
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("numbers", CommonUtils.data1.get("numbers"));
                        params.put("level", CommonUtils.data1.get("level"));
                        params.put("salary_range", CommonUtils.data1.get("salary_range"));
                        params.put("award_period", CommonUtils.data1.get("award_period"));
                        params.put("sex_demand", CommonUtils.data1.get("sex_demand"));
                        params.put("salary", CommonUtils.data1.get("salary"));
                        params.put("experience", CommonUtils.data1.get("experience"));
                        params.put("education", CommonUtils.data1.get("education"));
                        params.put("nation", CommonUtils.data1.get("nation"));
                        break;
                    case "42": //便民服务信息发布
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        break;
                    case "39": //车辆出租信息发布
                        params.put("sortid", CommonUtils.data1.get("sortId"));
                        params.put("abaility_estimate", CommonUtils.data1.get("abaility_estimate"));
                        params.put("years_estimate", CommonUtils.data1.get("years_estimate"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("car_type", CommonUtils.data1.get("car_type"));
                        params.put("car_brand", CommonUtils.data1.get("car_brand"));
                        params.put("car_speed", CommonUtils.data1.get("car_speed"));
                        params.put("price", CommonUtils.data1.get("price"));
                        break;
                    case "40": //物品出售信息发布
                        params.put("level", CommonUtils.data1.get("level"));
                        params.put("price", CommonUtils.data1.get("price"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        break;
                    case "107":
                        params.put("group", CommonUtils.data1.get("group"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        break;
                    case "44":
                        params.put("group", CommonUtils.data1.get("group"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("price", CommonUtils.data1.get("price"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        break;
                    case "48":
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("age", CommonUtils.data1.get("age"));
                        params.put("sex", CommonUtils.data1.get("sex"));
                        params.put("name", CommonUtils.data1.get("name"));
                        break;
                    case "74":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("period", CommonUtils.data1.get("period"));
                        params.put("price", CommonUtils.data1.get("price"));
                        params.put("group", CommonUtils.data1.get("group"));
                        break;
                    case "92":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("source", CommonUtils.data1.get("source"));
                        params.put("award_method", CommonUtils.data1.get("award_method"));
                        break;
                    case "94":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("company", CommonUtils.data1.get("company"));
                        params.put("price", CommonUtils.data1.get("price"));
                        break;
                    case "83":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("period", CommonUtils.data1.get("period"));
                        params.put("group", CommonUtils.data1.get("group"));
                        break;
                    case "50":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("order", CommonUtils.data1.get("order"));
                        break;
                }
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            CommonUtils.dismissProgress(progressDialog);
                            System.out.println("update:"+response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                if(!response.optString("message").equals("")) {
                                    Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 5);
    }
    private void uploadUserPhoto(long _tid) {
        if (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0] == null) {
            //Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateActivity.this, RoomDetailActivity.class);
            String fid = CommonUtils.data1.get("fId").toString();
            String sortid = CommonUtils.data1.get("sortId").toString();
            CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
            /*
            if(CommonUtils.share_bmp.getByteCount()>23000) {
                if (CommonUtils.share_bmp != null && !CommonUtils.share_bmp.isRecycled()) {
                    CommonUtils.share_bmp = Bitmap.createScaledBitmap(CommonUtils.share_bmp, 32, 32, true);
                }
            }
            */
            String url = APIManager.User_URL+"news/paper/"+Long.toString(_tid);
            intent.putExtra("fid", fid);
            intent.putExtra("sortid", sortid);
            intent.putExtra("newsId", Long.toString(_tid));
            intent.putExtra("title", CommonUtils.data1.get("title").toString());
            intent.putExtra("desc", CommonUtils.data1.get("message").toString());
            intent.putExtra("url", url);
            finish();
            UpdateActivity.this.startActivity(intent);
        }else{
            RequestParams params = new RequestParams();
            try {
                params.put("tid", String.valueOf(_tid));
                params.put("act", "upload");
                System.out.println(CommonUtils.mFiles.length);
                //params.put("liangyao_user_photo", new File(selectedImagePath), "image/png");
                for (int i = 0; i < CommonUtils.mFiles.length; i++) {
                    params.put("file["+i+"]", CommonUtils.mFiles[i]);
                    //params.put("file[]", CommonUtils.mFiles[i], "image/png");
                    System.out.println(i + "::" + CommonUtils.mFiles[i].getAbsolutePath());
                }

                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                String url = APIManager.Ucenter_URL;
                System.out.println(url + "iimmgg:" + _tid);
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateActivity.this, RoomDetailActivity.class);
                        String fid = CommonUtils.data1.get("fId").toString();
                        String sortid = CommonUtils.data1.get("sortId").toString();
                        CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        if(!CommonUtils.mFiles[0].getAbsolutePath().equals(""))
                            CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(CommonUtils.mFiles[0].getAbsolutePath());
                        if(CommonUtils.share_bmp==null)
                            CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        String url = APIManager.User_URL+"news/paper/"+CommonUtils.data1.get("tid").toString();
                        intent.putExtra("fid", fid);
                        intent.putExtra("sortid", sortid);
                        intent.putExtra("newsId", CommonUtils.data1.get("tid").toString());
                        intent.putExtra("title", CommonUtils.data1.get("title").toString());
                        intent.putExtra("desc", CommonUtils.data1.get("message").toString());
                        intent.putExtra("url", url);
                        for(int i=0;i<CommonUtils.mFiles.length;i++){
                            if(!(CommonUtils.mFiles[i].equals(null) || CommonUtils.mFiles[i].equals("")) && CommonUtils.mFiles[i].exists()){
                                CommonUtils.mFiles[i].delete();
                            }
                        }
                        finish();
                        UpdateActivity.this.startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println("img01+" + errorResponse);
                        //CommonUtils.dismissProgress(progressDialog);
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("img02+" + responseString);
                        //CommonUtils.dismissProgress(progressDialog);
                        Intent intent = new Intent(UpdateActivity.this, RoomDetailActivity.class);
                        String fid = CommonUtils.data1.get("fId").toString();
                        String sortid = CommonUtils.data1.get("sortId").toString();
                        CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        if(!CommonUtils.mFiles[0].getAbsolutePath().equals(""))
                            CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(CommonUtils.mFiles[0].getAbsolutePath());
                        if(CommonUtils.share_bmp==null)
                            CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        String url = APIManager.User_URL+"news/paper/"+CommonUtils.data1.get("tid").toString();
                        intent.putExtra("fid", fid);
                        intent.putExtra("sortid", sortid);
                        intent.putExtra("newsId", CommonUtils.data1.get("tid").toString());
                        intent.putExtra("title", CommonUtils.data1.get("title").toString());
                        intent.putExtra("desc", CommonUtils.data1.get("message").toString());
                        intent.putExtra("url", url);
                        for(int i=0;i<CommonUtils.mFiles.length;i++){
                            if(!(CommonUtils.mFiles[i].equals(null) || CommonUtils.mFiles[i].equals("")) && CommonUtils.mFiles[i].exists()){
                                CommonUtils.mFiles[i].delete();
                            }
                        }
                        finish();
                        UpdateActivity.this.startActivity(intent);
                    }
                });
            }catch (Exception e){

            }
        }
    }
    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(UpdateActivity.this, UserNewsActivity.class);
        //pActivity.startChildActivity("user_news", intent);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UpdateActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
        RelativeLayout rlt_post_data = (RelativeLayout) findViewById(R.id.rlt_post_data);
        rlt_post_data.setEnabled(true);
    }

}
